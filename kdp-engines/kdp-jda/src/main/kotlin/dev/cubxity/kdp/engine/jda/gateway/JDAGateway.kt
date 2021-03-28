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

package dev.cubxity.kdp.engine.jda.gateway

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.engine.jda.event.JDARawEvent
import dev.cubxity.kdp.engine.jda.event.message.guild.JDAGuildMessageCreateEvent
import dev.cubxity.kdp.engine.jda.event.message.guild.react.JDAGuildMessageReactionAddEvent
import dev.cubxity.kdp.engine.jda.event.message.guild.react.JDAGuildMessageReactionRemoveEvent
import dev.cubxity.kdp.event.Event
import dev.cubxity.kdp.gateway.Gateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.IEventManager
import kotlin.coroutines.CoroutineContext

@OptIn(KDPUnsafe::class)
class JDAGateway : Gateway, CoroutineScope, IEventManager {
    private val _eventFlow = MutableSharedFlow<Event>(extraBufferCapacity = Int.MAX_VALUE)

    override val coroutineContext: CoroutineContext =
        Dispatchers.Default + SupervisorJob()

    override val events: SharedFlow<Event>
        get() = _eventFlow

    override fun handle(event: GenericEvent) {
        val mappedEvent = when (event) {
            is GuildMessageReactionAddEvent -> JDAGuildMessageReactionAddEvent(event)
            is GuildMessageReactionRemoveEvent -> JDAGuildMessageReactionRemoveEvent(event)
            is GuildMessageReceivedEvent -> JDAGuildMessageCreateEvent(event)
            else -> JDARawEvent(event)
        }
        launch {
            _eventFlow.emit(mappedEvent)
        }
    }

    override fun register(listener: Any): Unit =
        throw UnsupportedOperationException()

    override fun unregister(listener: Any): Unit =
        throw UnsupportedOperationException()

    override fun getRegisteredListeners(): MutableList<Any> =
        throw UnsupportedOperationException()
}