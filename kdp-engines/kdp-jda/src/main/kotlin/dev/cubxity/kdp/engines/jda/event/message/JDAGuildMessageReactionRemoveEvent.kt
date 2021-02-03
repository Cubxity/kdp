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

package dev.cubxity.kdp.engines.jda.event.message

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.behavior.MessageBehavior
import dev.cubxity.kdp.engines.jda.behavior.JDAMemberBehavior
import dev.cubxity.kdp.engines.jda.behavior.JDAMessageBehavior
import dev.cubxity.kdp.engines.jda.behavior.JDAUserBehavior
import dev.cubxity.kdp.engines.jda.entity.*
import dev.cubxity.kdp.engines.jda.entity.channel.JDAGuildTextChannel
import dev.cubxity.kdp.engines.jda.event.shard
import dev.cubxity.kdp.engines.jda.util.await
import dev.cubxity.kdp.engines.jda.util.awaitOrNull
import dev.cubxity.kdp.entity.ReactionEmoji
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.asSnowflake
import dev.cubxity.kdp.event.message.ReactionAddEvent
import dev.cubxity.kdp.exception.channelNotFound
import dev.cubxity.kdp.exception.messageNotFound
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse

class JDAGuildMessageReactionRemoveEvent(
    override val kdp: KDP,
    private val event: GuildMessageReactionRemoveEvent
) : ReactionAddEvent {
    override val shard: Int
        get() = event.shard

    override val userId: Snowflake
        get() = event.userIdLong.asSnowflake()

    override val channelId: Snowflake
        get() = event.channel.snowflake

    override val messageId: Snowflake
        get() = event.messageIdLong.asSnowflake()

    override val guildId: Snowflake
        get() = event.guild.idLong.asSnowflake()

    override val user: JDAUserBehavior
        get() = JDAUserBehavior(kdp, event.userIdLong.asSnowflake(), event.jda)

    override val userAsMember: JDAMemberBehavior
        get() = JDAMemberBehavior(kdp, event.userIdLong.asSnowflake(), event.guild)

    override val channel: JDAGuildTextChannel
        get() = JDAGuildTextChannel(kdp, event.channel)

    override val message: MessageBehavior
        get() = JDAMessageBehavior(kdp, messageId, event.channel)

    override val guild: JDAGuild
        get() = JDAGuild(kdp, event.guild)

    override val emoji: ReactionEmoji
        get() = mapReaction()

    override suspend fun getUser(): JDAUser {
        try {
            return JDAUser(kdp, event.retrieveUser().await())
        } catch (exception: ErrorResponseException) {
            throw when (exception.errorResponse) {
                ErrorResponse.UNKNOWN_USER -> channelNotFound(userId)
                else -> exception
            }
        }
    }

    override suspend fun getUserOrNull(): JDAUser? =
        event.retrieveUser().awaitOrNull()?.let { JDAUser(kdp, it) }

    override suspend fun getUserAsMember(): JDAMember? =
        event.retrieveMember().awaitOrNull()?.let { JDAMember(kdp, it) }

    override suspend fun getChannelOrNull(): JDAGuildTextChannel = channel

    override suspend fun getMessage(): JDAMessage {
        try {
            return JDAMessage(kdp, event.retrieveMessage().await())
        } catch (exception: ErrorResponseException) {
            throw when (exception.errorResponse) {
                ErrorResponse.UNKNOWN_CHANNEL -> channelNotFound(channelId)
                ErrorResponse.UNKNOWN_MESSAGE -> messageNotFound(channelId, messageId)
                else -> exception
            }
        }
    }

    override suspend fun getMessageOrNull(): JDAMessage? =
        event.retrieveMessage().awaitOrNull()?.let { JDAMessage(kdp, it) }

    override suspend fun getGuild(): JDAGuild = guild

    private fun mapReaction(): ReactionEmoji = event.reactionEmote.run {
        when {
            isEmoji -> ReactionEmoji.Unicode(kdp, name)
            else -> ReactionEmoji.Custom(kdp, idLong.asSnowflake(), name, emote.isAnimated)
        }
    }
}