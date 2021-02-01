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

package dev.cubxity.kdp.behavior.channel

import dev.cubxity.kdp.entity.Message
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.exception.messageNotFound
import kotlinx.coroutines.flow.Flow

interface MessageChannelBehavior : ChannelBehavior {
    val messages: Flow<Message>

    suspend fun getMessage(messageId: Snowflake): Message =
        getMessageOrNull(messageId) ?: messageNotFound(id, messageId)

    suspend fun getMessageOrNull(messageId: Snowflake): Message?

    suspend fun deleteMessage(messageId: Snowflake)

    suspend fun triggerTyping()
}