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

package dev.cubxity.kdp.engine.jda.event.message.guild

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.behavior.GuildBehavior
import dev.cubxity.kdp.behavior.MessageBehavior
import dev.cubxity.kdp.behavior.channel.MessageChannelBehavior
import dev.cubxity.kdp.engine.jda.entity.JDAGuild
import dev.cubxity.kdp.engine.jda.entity.channel.JDAGuildMessageChannel
import dev.cubxity.kdp.engine.jda.event.guild.JDAGuildEvent
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.asSnowflake
import dev.cubxity.kdp.event.message.guild.GuildMessageEvent
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent

@KDPUnsafe
interface JDAGuildMessageEvent : JDAGuildEvent, GuildMessageEvent {
    override val event: GenericGuildMessageEvent

    override val guild: GuildBehavior
        get() = JDAGuild(event.guild)

    override val message: MessageBehavior
        get() = MessageBehaviorImpl(event)

    override val channel: MessageChannelBehavior
        get() = JDAGuildMessageChannel(event.channel)

    private inline class MessageBehaviorImpl(private val event: GenericGuildMessageEvent) : MessageBehavior {
        override val id: Snowflake
            get() = event.messageIdLong.asSnowflake()

        override val channel: MessageChannelBehavior
            get() = JDAGuildMessageChannel(event.channel)
    }
}