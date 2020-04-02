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
import club.minnced.jda.reactor.toText
import dev.cubxity.libs.kdp.KDP
import dev.cubxity.libs.kdp.command.Command
import dev.cubxity.libs.kdp.command.SubCommand
import dev.cubxity.libs.kdp.feature.KDPFeature
import dev.cubxity.libs.kdp.feature.install
import kotlinx.coroutines.*
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent

class Processor(val kdp: KDP) : CoroutineScope {
    override val coroutineContext = Job() + Dispatchers.Default
    private val ARGS_REGEX = "(\"([^\"]+)\"|[^ ]+)( ?)".toRegex()
    private val ARGS_REGEX_NO_QUOTES = "(([^ ]+)|[^ ]+)( ?)".toRegex()

    /**
     * Only works if [prefixFactory] has not been changed
     */
    var prefix = "."

    /**
     * Determines whether errors should be logged
     */
    var printError = true

    /**
     * Factory to provide prefixes for the specified message event
     */
    var prefixFactory: PrefixFactory = SimplePrefixFactory { listOf(prefix) }

    fun processEvent(e: MessageReceivedEvent) {
        launch {
            kdp.execute(
                    CommandProcessingContext(kdp, e.author, e.channel, e.message, e),
                    CommandProcessingPipeline.PRE_FILTER,
                    CommandProcessingPipeline.MATCH,
                    CommandProcessingPipeline.POST_FILTER,
                    CommandProcessingPipeline.MONITORING,
                    CommandProcessingPipeline.PROCESS
            )
        }
    }

    fun processEvent(e: MessageUpdateEvent) {
        launch {
            kdp.execute(
                    CommandProcessingContext(kdp, e.author, e.channel, e.message, e),
                    CommandProcessingPipeline.PRE_FILTER,
                    CommandProcessingPipeline.MATCH,
                    CommandProcessingPipeline.POST_FILTER,
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
                    this.prefix = prefix

                    var args = processArguments(content, prefix, ARGS_REGEX)
                    if (args.isEmpty()) {
                        finish()
                        return@with
                    }

                    val cmdName = args[0]
                    val cmd = kdp.moduleManager.modules.mapNotNull { it.commands.find { c -> cmdName in c.aliases } }
                            .firstOrNull()
                    if (cmd == null) {
                        finish()
                        return@with
                    }
                    this.alias = cmdName
                    args =
                            (if (cmd.ignoreQuotes) processArguments(content, prefix, ARGS_REGEX_NO_QUOTES) else args).let {
                                it.subList(1, it.size)
                            }

                    var subCommand: SubCommand? = null
                    var depth = 0
                    for (a in args) {
                        val c = subCommand ?: cmd
                        val sc = c.subCommands.find { a in it.aliases } ?: break
                        subCommand = sc
                        depth++
                    }

                    if (subCommand?.ignoreQuotes == true)
                        args = processArguments(content, prefix, ARGS_REGEX_NO_QUOTES).let { it.subList(1, it.size) }

                    if (depth > 0) args = args.subList(depth, args.size)
                    val effectiveCommand = subCommand ?: cmd

                    val requiredArgs = effectiveCommand.args?.filter { it.required }
                    if (requiredArgs != null && args.size < requiredArgs.size)
                        throw MissingArgumentException(requiredArgs[args.size].name, effectiveCommand)

                    this.command = effectiveCommand
                    this.rawArgs = args
                } catch (t: Throwable) {
                    if (printError) t.printStackTrace()
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
                    if (printError) t.printStackTrace()
                    exception = t
                    finish()
                    kdp.execute(this, CommandProcessingPipeline.ERROR)
                }
            }
        }
    }

    private fun processArguments(content: String, alias: String, regex: Regex) =
            regex.findAll(content.removePrefix(alias))
                    .mapNotNull {
                        it.groupValues.getOrNull(2)?.let { s -> if (s.isEmpty()) it.groupValues.getOrNull(1) else s }
                                ?: it.groupValues.getOrNull(1)
                    }
                    .toList()

    companion object Feature : KDPFeature<KDP, Processor, Processor> {
        override val key = "kdp.features.processor"

        const val FLAG_IGNORE_QUOTES = "core:ignore_quotes"

        override fun install(pipeline: KDP, configure: Processor.() -> Unit): Processor {
            val feature = Processor(pipeline).apply(configure)
            with(pipeline.manager) {
                on<MessageReceivedEvent>().subscribe { feature.processEvent(it) }
                on<MessageUpdateEvent>().subscribe { feature.processEvent(it) }
            }
            return feature
        }
    }
}

var Command.ignoreQuotes: Boolean
    get() = flags[Processor.FLAG_IGNORE_QUOTES] as? Boolean ?: false
    set(value) {
        flags[Processor.FLAG_IGNORE_QUOTES] = value
    }

/**
 * Gets the text attachments as a string.
 *
 * @author Koding
 * @since  0.1-PRE
 */
suspend fun Message.textAttachments() =
        // Run as IO
        withContext(Dispatchers.IO) {
            // Filter valid attachments
            attachments.filter { !it.isImage && !it.isVideo }
                    // Get all the text
                    .mapNotNull {
                        async {
                            // Block with context
                            withContext(Dispatchers.IO) { it.toText().block() }
                        }
                    }
                    // Await all
                    .awaitAll()
                    // Join it
                    .joinToString(separator = " ")
        }

/**
 * Get or install [Processor] feature and run [opt] on it
 */
fun KDP.processing(opt: Processor.() -> Unit = {}): Processor = (features[Processor.key] as Processor?
        ?: install(Processor)).apply(opt)