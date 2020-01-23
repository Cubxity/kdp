/*
 *     KDP is a modular and customizable Discord command processing library.
 *     Copyright (C) 2020  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.processing

import club.minnced.jda.reactor.on
import dev.cubxity.libs.kdp.KDP
import dev.cubxity.libs.kdp.feature.KDPFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class Processor(val kdp: KDP) : CoroutineScope {
    override val coroutineContext = Job() + Dispatchers.Default

    fun processEvent(e: GuildMessageReceivedEvent) {

    }

    companion object Feature : KDPFeature<KDP, Processor, Processor> {
        override val key = "kdp.features.processor"

        override fun install(pipeline: KDP, configure: Processor.() -> Unit): Processor {
            val feature = Processor(pipeline)
            pipeline
                .on<GuildMessageReceivedEvent>()
                .subscribe { feature.processEvent(it) }
            return feature
        }
    }
}