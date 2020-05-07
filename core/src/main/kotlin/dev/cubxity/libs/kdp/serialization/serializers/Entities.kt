/*
 * KDP is a modular and customizable Discord command processing library.
 * Copyright (C) 2020 Cubxity.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.serialization.serializers

import dev.cubxity.libs.kdp.processing.CommandProcessingContext
import dev.cubxity.libs.kdp.serialization.ArgumentSerializer
import dev.cubxity.libs.kdp.utils.FuzzyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MemberSerializer(private val flags: Int = DEFAULT_FLAGS) : ArgumentSerializer<Member> {
    companion object {
        const val FUZZY = 0b1

        const val DEFAULT_FLAGS = FUZZY

        private val REGEX =
            "(\\d{1,19})|<@(!?)(\\d{1,19})>|@?(?<name>[^#]{2,32})(?:#(?<discriminator>\\d{4}))?".toRegex()
    }

    private val isFuzzy
        get() = flags and FUZZY != 0

    override suspend fun serialize(ctx: CommandProcessingContext, s: String): Member? {
        val match = REGEX.matchEntire(s)?.groupValues ?: return null

        val id = match.getOrNull(1)?.takeIf { it.isNotEmpty() } ?: match.getOrNull(3)?.takeIf { it.isNotEmpty() }
        return if (id != null)
            ctx.guild?.getMemberById(id.toLongOrNull() ?: return null)
                ?: withContext(Dispatchers.IO) { ctx.guild?.retrieveMemberById(id)?.complete() }
        else {
            val n = match[4]
            val d = match.getOrNull(5)?.takeIf { it.isNotEmpty() }
            ctx.guild?.memberCache?.let {
                it.find { m -> m.user.name == n && (d == null || m.user.discriminator == d) }
                    ?: (if (isFuzzy) FuzzyUtils.extract(n, it.asList(), Member::getEffectiveName)?.item else null)
                    ?: suspendCoroutine<Member?> { cont ->
                        ctx.guild?.retrieveMembersByPrefix(n, 100)?.onSuccess { l ->
                            cont.resume(l?.firstOrNull())
                        }
                    }
            }
        }
    }
}

class UserSerializer(private val flags: Int = DEFAULT_FLAGS) : ArgumentSerializer<User> {
    companion object {
        const val FUZZY = 0b1

        const val DEFAULT_FLAGS = FUZZY

        private val REGEX =
            "(\\d{1,19})|<@(!?)(\\d{1,19})>|@?(?<name>[^#]{2,32})(?:#(?<discriminator>\\d{4}))?".toRegex()
    }

    private val isFuzzy
        get() = flags and FUZZY != 0

    override suspend fun serialize(ctx: CommandProcessingContext, s: String): User? {
        val match = REGEX.matchEntire(s)?.groupValues ?: return null

        val id = match.getOrNull(1)?.takeIf { it.isNotEmpty() }
            ?: match.getOrNull(3)?.takeIf { it.isNotEmpty() }
        return if (id != null)
            ctx.event.jda.getUserById(id.toLongOrNull() ?: return null)
                ?: withContext(Dispatchers.IO) { ctx.event.jda.retrieveUserById(id).complete() }
        else {
            val n = match[4]
            val d = match.getOrNull(5)?.takeIf { it.isNotEmpty() }
            val users = ctx.guild?.memberCache?.mapNotNull { it.user }
            users?.find { it.name == n && (d == null || it.discriminator == d) }
                ?: (if (isFuzzy) FuzzyUtils.extract(n, users ?: listOf(), User::getName)?.item else null)
                ?: suspendCoroutine { cont ->
                    ctx.guild?.retrieveMembersByPrefix(n, 100)?.onSuccess {
                        cont.resume(it?.firstOrNull()?.user)
                    }
                }
        }
    }
}

class ChannelSerializer(private val flags: Int = DEFAULT_FLAGS) : ArgumentSerializer<MessageChannel> {
    companion object {
        const val FUZZY = 0b1
        const val DEFAULT_FLAGS = FUZZY

        private val REGEX = "(?<id>\\d{1,19})|<#(!?)(?<tag>\\d{1,19})>|#?(?<name>[^#]*)?".toRegex()
    }

    private val isFuzzy
        get() = flags and FUZZY != 0

    override suspend fun serialize(ctx: CommandProcessingContext, s: String): MessageChannel? {
        val match = REGEX.matchEntire(s)?.groups ?: return null

        val id = match["id"] ?: match["tag"]
        return if (id != null)
            ctx.event.jda.getTextChannelById(id.value.toLongOrNull() ?: return null)
        else {
            val name = match["name"]?.value ?: return null
            val channels = ctx.guild?.textChannels
            channels?.find { it.name.equals(name, true) }
                ?: if (isFuzzy) FuzzyUtils.extract(name, channels ?: listOf(), MessageChannel::getName)?.item
                else null
        }
    }
}

class RoleSerializer(private val flags: Int = DEFAULT_FLAGS) : ArgumentSerializer<Role> {
    companion object {
        const val FUZZY = 0b1
        const val DEFAULT_FLAGS = FUZZY

        private val REGEX = "(?<id>\\d{1,19})|<@&(!?)(?<tag>\\d{1,19})>|(?<name>[^#]*)?".toRegex()
    }

    private val isFuzzy
        get() = flags and FUZZY != 0

    override suspend fun serialize(ctx: CommandProcessingContext, s: String): Role? {
        val match = REGEX.matchEntire(s)?.groups ?: return null

        val id = match["id"]?.value?.toLongOrNull() ?: match["tag"]?.value?.toLongOrNull()
        return if (id != null)
            ctx.event.jda.getRoleById(id)
        else {
            val name = match["name"]?.value ?: return null
            val roles = ctx.guild?.roles
            roles?.find { it.name.equals(name, true) }
                ?: if (isFuzzy) FuzzyUtils.extract(name, roles ?: listOf(), Role::getName)?.item
                else null
        }
    }
}
