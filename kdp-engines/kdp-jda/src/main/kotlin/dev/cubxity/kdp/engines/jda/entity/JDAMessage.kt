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

package dev.cubxity.kdp.engines.jda.entity

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.engines.jda.entity.channel.JDAGuildTextChannel
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.User
import dev.cubxity.kdp.entity.channel.MessageChannel
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import dev.cubxity.kdp.entity.Message as KDPMessage

class JDAMessage(override val kdp: KDP, private val message: Message) : KDPMessage {
    override val id: Snowflake
        get() = message.snowflake

    override val channelId: Snowflake
        get() = message.channel.snowflake

    override val channel: MessageChannel
        get() = when (val channel = message.channel) {
            is TextChannel -> JDAGuildTextChannel(kdp, channel)
            else -> TODO("Not yet implemented")
        }

    override val author: User
        get() = JDAUser(kdp, message.author)

    override val content: String
        get() = message.contentRaw

    override suspend fun getChannel() = channel

    override suspend fun getChannelOrNull() = channel

    override suspend fun asMessage(): JDAMessage = this

    override suspend fun asMessageOrNull(): JDAMessage = this
}