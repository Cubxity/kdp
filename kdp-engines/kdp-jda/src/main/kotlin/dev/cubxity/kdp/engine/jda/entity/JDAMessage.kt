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

package dev.cubxity.kdp.engine.jda.entity

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.engine.jda.entity.channel.JDAGuildMessageChannel
import dev.cubxity.kdp.entity.Message
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.User
import dev.cubxity.kdp.entity.channel.MessageChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.Message as JDAMessage

@KDPUnsafe
inline class JDAMessage(private val message: JDAMessage) : Message {
    override val id: Snowflake
        get() = message.snowflake

    override val channel: MessageChannel
        get() = when (val channel = message.channel) {
            is TextChannel -> JDAGuildMessageChannel(channel)
            else -> TODO("Not yet implemented")
        }

    override val author: User
        get() = JDAUser(message.author)

    override val content: String
        get() = message.contentRaw
}