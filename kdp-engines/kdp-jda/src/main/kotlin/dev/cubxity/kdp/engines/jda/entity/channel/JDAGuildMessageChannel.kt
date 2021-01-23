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

package dev.cubxity.kdp.engines.jda.entity.channel

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.engines.jda.JDAEngine
import dev.cubxity.kdp.engines.jda.entity.JDAMessage
import dev.cubxity.kdp.engines.jda.entity.snowflake
import dev.cubxity.kdp.engines.jda.util.await
import dev.cubxity.kdp.engines.jda.util.awaitOrNull
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.exception.channelNotFound
import dev.cubxity.kdp.exception.messageNotFound
import kotlinx.coroutines.flow.Flow
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse
import dev.cubxity.kdp.entity.channel.GuildMessageChannel as KDPGuildMessageChannel

class JDAGuildMessageChannel(
    kdp: KDP<JDAEngine>,
    private val channel: TextChannel
) : JDAGuildChannel(kdp, channel), KDPGuildMessageChannel<JDAEngine> {
    override val guildId: Snowflake
        get() = channel.guild.snowflake

    override val messages: Flow<JDAMessage>
        get() = TODO("Not yet implemented")

    override suspend fun getMessage(messageId: Snowflake): JDAMessage {
        try {
            return JDAMessage(kdp, channel.retrieveMessageById(messageId.value).await())
        } catch (exception: ErrorResponseException) {
            throw when (exception.errorResponse) {
                ErrorResponse.UNKNOWN_CHANNEL -> channelNotFound(id)
                ErrorResponse.UNKNOWN_MESSAGE -> messageNotFound(id, messageId)
                else -> exception
            }
        }
    }

    override suspend fun getMessageOrNull(messageId: Snowflake): JDAMessage? =
        channel.retrieveMessageById(messageId.value).awaitOrNull()?.let { JDAMessage(kdp, it) }

    override suspend fun deleteMessage(messageId: Snowflake) {
        try {
            channel.deleteMessageById(messageId.value).await()
        } catch (exception: ErrorResponseException) {
            throw when (exception.errorResponse) {
                ErrorResponse.UNKNOWN_CHANNEL -> channelNotFound(id)
                ErrorResponse.UNKNOWN_MESSAGE -> messageNotFound(id, messageId)
                else -> exception
            }
        }
    }

    override suspend fun triggerTyping() {
        channel.sendTyping().await()
    }
}