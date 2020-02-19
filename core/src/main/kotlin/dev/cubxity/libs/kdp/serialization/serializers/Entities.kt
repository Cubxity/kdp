/*
 *     KDP is a modular and customizable Discord command processing library.
 *     Copyright (C) 2020  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.serialization.serializers

import dev.cubxity.libs.kdp.processing.CommandProcessingContext
import dev.cubxity.libs.kdp.serialization.ArgumentSerializer
import dev.cubxity.libs.kdp.utils.FuzzyUtils
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User

class MemberSerializer(private val flags: Int = DEFAULT_FLAGS) : ArgumentSerializer<Member> {
    companion object {
        const val FUZZY = 0b1

        const val DEFAULT_FLAGS = FUZZY

        private val REGEX =
            "(\\d{1,19})|<@(!?)(\\d{1,19})>|@?(?<name>[^#]{2,32})(?:#(?<discriminator>\\d{4}))?".toRegex()
    }

    private val isFuzzy
        get() = flags and FUZZY != 0

    override fun serialize(ctx: CommandProcessingContext, s: String): Member? {
        val match = REGEX.matchEntire(s)?.groupValues ?: return null

        val id = match.getOrNull(1)?.takeIf { it.isNotEmpty() } ?: match.getOrNull(3)?.takeIf { it.isNotEmpty() }
        return if (id != null)
            ctx.guild?.getMemberById(id)
        else {
            val n = match[4]
            val d = match.getOrNull(5)?.takeIf { it.isNotEmpty() }
            ctx.guild?.memberCache?.let {
                it.find { m -> m.user.name == n && (d == null || m.user.discriminator == d) }
                    ?: if (isFuzzy) FuzzyUtils.extract(n, it, Member::getEffectiveName)?.item
                    else null
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
        get() = flags and MemberSerializer.FUZZY != 0

    override fun serialize(ctx: CommandProcessingContext, s: String): User? {
        val match = REGEX.matchEntire(s)?.groupValues ?: return null

        val id = match.getOrNull(1)?.takeIf { it.isNotEmpty() } ?: match.getOrNull(3)?.takeIf { it.isNotEmpty() }
        return if (id != null)
            ctx.event.jda.getUserById(id)
        else {
            val n = match[4]
            val d = match.getOrNull(5)?.takeIf { it.isNotEmpty() }
            val users = ctx.guild?.memberCache?.mapNotNull { it.user }
            users?.find { it.name == n && (d == null || it.discriminator == d) }
                ?: if (isFuzzy) FuzzyUtils.extract(n, users ?: listOf(), User::getName)?.item
                else null
        }
    }
}