/*
 *  KDP is a modular and customizable Discord command processing library.
 *  Copyright (C) 2020-2021 Cubxity.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.kdp.engine.jda.supplier

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.engine.jda.entity.*
import dev.cubxity.kdp.engine.jda.entity.channel.JDAGuildMessageChannel
import dev.cubxity.kdp.engine.jda.util.await
import dev.cubxity.kdp.engine.jda.util.awaitOrNull
import dev.cubxity.kdp.entity.*
import dev.cubxity.kdp.entity.channel.Channel
import dev.cubxity.kdp.entity.channel.GuildChannel
import dev.cubxity.kdp.supplier.EntitySupplier
import dev.cubxity.kdp.supplier.Position
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel

@KDPUnsafe
inline class JDAEntitySupplier(private val jda: JDA) : EntitySupplier {
    override val guilds: Flow<Guild>
        get() = flow {
            for (guild in jda.guildCache) {
                emit(JDAGuild(guild))
            }
        }

    override suspend fun getGuildOrNull(id: Snowflake): Guild? = mapExceptions {
        jda.getGuildById(id.value)?.let { JDAGuild(it) }
    }

    override suspend fun getChannelOrNull(id: Snowflake): Channel? = mapExceptions {
        jda.getTextChannelById(id.value)?.let { JDAGuildMessageChannel(it) }
        // TODO: Other channel types
    }

    override fun getGuildChannels(guildId: Snowflake): Flow<GuildChannel> = flow {
        val channels = jda.getGuildById(guildId.value)?.channels ?: return@flow
        for (channel in channels) {
            when (channel) {
                is TextChannel -> JDAGuildMessageChannel(channel)
                else -> {
                    // TODO
                }
            }
        }
    }

    override fun getChannelPins(channelId: Snowflake): Flow<Message> = flow {
        val channel = jda.getTextChannelById(channelId.value)
            ?: jda.getPrivateChannelById(channelId.value) ?: return@flow

        for (message in channel.retrievePinnedMessages().await()) {
            emit(JDAMessage(message))
        }
    }

    override suspend fun getMemberOrNull(guildId: Snowflake, userId: Snowflake): Member? =
        jda.getGuildById(guildId.value)?.retrieveMemberById(userId.value)?.awaitOrNull()?.let { JDAMember(it) }

    override suspend fun getMessageOrNull(channelId: Snowflake, messageId: Snowflake): Message? {
        val channel = jda.getTextChannelById(channelId.value)
            ?: jda.getPrivateChannelById(channelId.value) ?: return null

        return channel.retrieveMessageById(messageId.value).awaitOrNull()?.let { JDAMessage(it) }
    }

    override fun getMessages(channelId: Snowflake, position: Position, limit: Int): Flow<Message> = flow {
        val channel = jda.getTextChannelById(channelId.value)
            ?: jda.getPrivateChannelById(channelId.value) ?: return@flow

        // TODO: Paginate
    }

    override suspend fun getSelfOrNull(): User =
        JDAUser(jda.selfUser)

    override suspend fun getUserOfNull(id: Snowflake): User? =
        jda.getUserById(id.value)?.let { JDAUser(it) }

    override suspend fun getRoleOrNull(guildId: Snowflake, roleId: Snowflake): Role? =
        jda.getRoleById(roleId.value)?.let { JDARole(it) }

    override fun getGuildRoles(guildId: Snowflake): Flow<Role> = flow {
        val roles = jda.getGuildById(guildId.value)?.roleCache ?: return@flow
        for (role in roles) {
            emit(JDARole(role))
        }
    }

    override fun getGuildMembers(guildId: Snowflake, limit: Int): Flow<Member> = flow {
        val members = jda.getGuildById(guildId.value)?.members ?: return@flow
        for (member in members) {
            emit(JDAMember(member))
        }
    }

    override fun getEmojis(guildId: Snowflake): Flow<Emoji> = flow {
        val emotes = jda.getGuildById(guildId.value)?.emoteCache ?: return@flow
        for (emote in emotes) {
            emit(JDAEmoji(emote))
        }
    }

    inline fun <T> mapExceptions(block: () -> T): T {
        // TODO: Remap JDA exceptions
        return block()
    }
}