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

package dev.cubxity.kdp.engine

import dev.cubxity.kdp.event.Event
import dev.cubxity.kdp.gateway.GatewayScope
import dev.cubxity.kdp.gateway.Intents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface KDPGatewayEngine<TEngine : KDPEngine<TEngine>> : KDPEngine<TEngine>, GatewayScope {
    interface Configuration : KDPEngine.Configuration {
        /**
         * Enabled gateway [intents][Intents].
         */
        var intents: Intents
    }

    /**
     * Logs in to the Discord gateway. Suspends until engine shutdown.
     * See: https://discord.com/developers/docs/topics/gateway.
     */
    suspend fun login()
}

inline fun <reified T : Event> KDPGatewayEngine<*>.on(
    scope: CoroutineScope = kdp,
    noinline block: suspend T.() -> Unit
): Job {
    return gateway.events.buffer(Channel.UNLIMITED)
        .filterIsInstance<T>()
        .onEach(block)
        .launchIn(scope)
}