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
import dev.cubxity.kdp.behavior.channel.MessageChannelBehavior
import dev.cubxity.kdp.engine.KDPEngine
import dev.cubxity.kdp.entity.Guild
import dev.cubxity.kdp.entity.Message
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.channel.MessageChannel
import dev.cubxity.kdp.event.Event
import dev.cubxity.kdp.exception.channelNotFound

interface MessageDeleteEvent<TEngine : KDPEngine<TEngine>> : Event<TEngine> {
    val messageId: Snowflake

    val channelId: Snowflake

    val guildId: Snowflake?

    val message: Message<TEngine>?

    val channel: MessageChannelBehavior<TEngine>?

    val guild: GuildBehavior<TEngine>?

    suspend fun getChannel(): MessageChannel<TEngine> =
        getChannelOrNull() ?: channelNotFound(channelId)

    suspend fun getChannelOrNull(): MessageChannel<TEngine>?

    suspend fun getGuild(): Guild<TEngine>?
}