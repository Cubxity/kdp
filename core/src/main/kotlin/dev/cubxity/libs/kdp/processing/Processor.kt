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
import dev.cubxity.libs.kdp.command.SubCommand
import dev.cubxity.libs.kdp.feature.KDPFeature
import dev.cubxity.libs.kdp.feature.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent

class Processor(val kdp: KDP) : CoroutineScope {
    override val coroutineContext = Job() + Dispatchers.Default
    private val ARGS_REGEX = "(\"([^\"]+)\"|[^ ]+)( ?)".toRegex()

    /**
     * Only works if [prefixFactory] has not been changed
     */
    var prefix = "."

    /**
     * Factory to provide prefixes for the specified message event
     */
    var prefixFactory: PrefixFactory = MergedPrefixFactory { listOf(prefix) }

    fun processEvent(e: MessageReceivedEvent) {
        launch {
            kdp.execute(
                CommandProcessingContext(kdp, e.author, e.channel, e.message, e),
                CommandProcessingPipeline.FILTER,
                CommandProcessingPipeline.MATCH,
                CommandProcessingPipeline.MONITORING,
                CommandProcessingPipeline.PROCESS
            )
        }
    }

    fun processEvent(e: MessageUpdateEvent) {
        launch {
            kdp.execute(
                CommandProcessingContext(kdp, e.author, e.channel, e.message, e),
                CommandProcessingPipeline.FILTER,
                CommandProcessingPipeline.MATCH,
                CommandProcessingPipeline.MONITORING,
                CommandProcessingPipeline.PROCESS
            )
        }
    }

    init {
        kdp.intercept(CommandProcessingPipeline.MATCH) {
            with(context) {
                try {
                    val content = message.contentRaw
                    if (content.isEmpty()) {
                        finish()
                        return@with
                    }

                    val prefixes = when (event) {
                        is MessageReceivedEvent -> prefixFactory.get(event)
                        is MessageUpdateEvent -> prefixFactory.get(event)
                        else -> { // This should not happen
                            finish()
                            return@with
                        }
                    }
                    val prefix = prefixes.find { content.startsWith(it) }
                    if (prefix == null) {
                        finish()
                        return@with
                    }

                    var args = processArguments(content, prefix)
                    if (args.isEmpty()) {
                        finish()
                        return@with
                    }

                    val cmdName = args[0]
                    val cmd = kdp.moduleManager.modules.map { it.commands.find { c -> cmdName in c.aliases } }
                        .firstOrNull()
                    if (cmd == null) {
                        finish()
                        return@with
                    }
                    this.alias = cmdName
                    args = args.subList(1, args.size)

                    var subCommand: SubCommand? = null
                    var depth = 0
                    for (a in args) {
                        val c = subCommand ?: cmd
                        val sc = c.subCommands.find { a in it.aliases } ?: break
                        subCommand = sc
                        depth++
                    }
                    if (depth > 0) args = args.subList(depth, args.size)
                    val effectiveCommand = subCommand ?: cmd

                    this.command = effectiveCommand
                    this.args = args
                } catch (t: Throwable) {
                    t.printStackTrace()
                    exception = t
                    finish()
                    kdp.execute(this, CommandProcessingPipeline.ERROR)
                }
            }
        }
        kdp.intercept(CommandProcessingPipeline.PROCESS) {
            with(context) {
                try {
                    val cmd = command
                    if (cmd == null) {
                        finish()
                        return@with
                    }

                    cmd.handler?.invoke(this)
                } catch (t: Throwable) {
                    exception = t
                    finish()
                    kdp.execute(this, CommandProcessingPipeline.ERROR)
                }
            }
        }
    }

    private fun processArguments(content: String, alias: String) =
        ARGS_REGEX.findAll(content.removePrefix(alias))
            .mapNotNull {
                it.groupValues.getOrNull(2)?.let { s -> if (s.isEmpty()) it.groupValues.getOrNull(1) else s }
                    ?: it.groupValues.getOrNull(1)
            }
            .toList()

    companion object Feature : KDPFeature<KDP, Processor, Processor> {
        override val key = "kdp.features.processor"

        override fun install(pipeline: KDP, configure: Processor.() -> Unit): Processor {
            val feature = Processor(pipeline)
            with(pipeline.manager) {
                on<MessageReceivedEvent>().subscribe { feature.processEvent(it) }
                on<MessageUpdateEvent>().subscribe { feature.processEvent(it) }
            }
            return feature
        }
    }
}

/**
 * Get or install [Processor] feature and run [opt] on it
 */
fun KDP.processing(opt: Processor.() -> Unit = {}): Processor = (features[Processor.key] as Processor?
    ?: install(Processor)).apply(opt)