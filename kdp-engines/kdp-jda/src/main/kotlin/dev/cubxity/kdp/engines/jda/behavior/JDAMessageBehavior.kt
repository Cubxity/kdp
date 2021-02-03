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

package dev.cubxity.kdp.engines.jda.behavior

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.behavior.MessageBehavior
import dev.cubxity.kdp.engines.jda.entity.JDAMessage
import dev.cubxity.kdp.engines.jda.entity.channel.JDAGuildTextChannel
import dev.cubxity.kdp.engines.jda.entity.snowflake
import dev.cubxity.kdp.engines.jda.util.await
import dev.cubxity.kdp.engines.jda.util.awaitOrNull
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.exception.channelNotFound
import dev.cubxity.kdp.exception.messageNotFound
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse
import dev.cubxity.kdp.entity.channel.MessageChannel as KDPMessageChannel

class JDAMessageBehavior(
    override val kdp: KDP,
    override val id: Snowflake,
    private val messageChannel: MessageChannel
) : MessageBehavior {
    override val channelId: Snowflake
        get() = messageChannel.snowflake

    override val channel: KDPMessageChannel
        get() = when (messageChannel) {
            is TextChannel -> JDAGuildTextChannel(kdp, messageChannel)
            else -> TODO()
        }

    override suspend fun getChannel(): KDPMessageChannel = channel

    override suspend fun getChannelOrNull(): KDPMessageChannel = channel

    override suspend fun asMessage(): JDAMessage {
        try {
            return JDAMessage(kdp, messageChannel.retrieveMessageById(id.value).await())
        } catch (exception: ErrorResponseException) {
            throw when (exception.errorResponse) {
                ErrorResponse.UNKNOWN_CHANNEL -> channelNotFound(channelId)
                ErrorResponse.UNKNOWN_MESSAGE -> messageNotFound(channelId, id)
                else -> exception
            }
        }
    }

    override suspend fun asMessageOrNull(): JDAMessage? =
        messageChannel.retrieveMessageById(id.value).awaitOrNull()?.let { JDAMessage(kdp, it) }
}