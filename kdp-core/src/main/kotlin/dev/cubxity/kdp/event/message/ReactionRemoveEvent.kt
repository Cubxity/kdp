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

package dev.cubxity.kdp.event.message

import dev.cubxity.kdp.behavior.GuildBehavior
import dev.cubxity.kdp.behavior.MemberBehavior
import dev.cubxity.kdp.behavior.MessageBehavior
import dev.cubxity.kdp.behavior.UserBehavior
import dev.cubxity.kdp.behavior.channel.MessageChannelBehavior
import dev.cubxity.kdp.entity.*
import dev.cubxity.kdp.entity.channel.MessageChannel
import dev.cubxity.kdp.event.Event
import dev.cubxity.kdp.exception.channelNotFound
import dev.cubxity.kdp.exception.messageNotFound
import dev.cubxity.kdp.exception.userNotFound

interface ReactionRemoveEvent : Event {
    val userId: Snowflake

    val channelId: Snowflake

    val messageId: Snowflake

    val guildId: Snowflake?

    val user: UserBehavior

    val userAsMember: MemberBehavior?

    val channel: MessageChannelBehavior

    val message: MessageBehavior

    val guild: GuildBehavior?

    val emoji: ReactionEmoji

    suspend fun getUser(): User =
        getUserOrNull() ?: userNotFound(userId)

    suspend fun getUserOrNull(): User?

    suspend fun getUserAsMember(): Member?

    suspend fun getChannel(): MessageChannel =
        getChannelOrNull() ?: channelNotFound(channelId)

    suspend fun getChannelOrNull(): MessageChannel?

    suspend fun getMessage(): Message =
        getMessageOrNull() ?: messageNotFound(channelId, messageId)

    suspend fun getMessageOrNull(): Message?

    suspend fun getGuild(): Guild?
}