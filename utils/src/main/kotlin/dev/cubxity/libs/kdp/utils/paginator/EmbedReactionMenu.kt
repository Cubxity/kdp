/*
 * KDP is a modular and customizable Discord command processing library.
 * Copyright (C) 2020 Cubxity.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.utils.paginator

import club.minnced.jda.reactor.on
import dev.cubxity.libs.kdp.processing.CommandProcessingContext
import kotlinx.coroutines.*
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.exceptions.ContextException
import reactor.core.Disposable
import java.time.Duration
import java.util.concurrent.TimeoutException
import kotlin.math.max
import kotlin.math.min

class EmbedReactionMenu(
    private val embeds: Array<MessageEmbed>,
    private val timeout: Duration = Duration.ofSeconds(15),
    private val reactions: PaginatorReactions = PaginatorReactions(),
    private val footer: String? = null,
    private val delete: Boolean = true,
    private val appendFooter: Boolean = true,
    private val editMessage: Message? = null,
    private val clearContent: Boolean = true
) : CoroutineScope {
    override val coroutineContext = Dispatchers.Default + Job()
    private var listener: Disposable? = null
    private var index = 0
    private var msg: Message? = null

    /**
     * @param page page index
     */
    suspend fun sendTo(ctx: CommandProcessingContext, page: Int = 0) {
        val messageEmbed = embeds[page]
        val clone = EmbedBuilder(messageEmbed)

        val footer = when {
            appendFooter -> messageEmbed.footer?.text?.let { " | $it" } ?: ""
            footer != null -> " | $footer"
            else -> ""
        }
        clone.setFooter("Page ${page + 1} / ${embeds.size}$footer", ctx.executor.effectiveAvatarUrl)

        send(
            ctx,
            MessageBuilder(clone)
                .let { if (clearContent) it.setContent(" ") else it }
                .build(),
            page
        )
        index = page
    }

    @Suppress("SuspendFunctionOnCoroutineScope")
    private suspend fun send(ctx: CommandProcessingContext, embed: Message, page: Int) {
        val first = msg == null
        val msg = msg ?: editMessage
        // TODO
        // Not sending via ctx.send because I have not implemented ctx.edit
        val m = if (msg != null) {
            if (first || index != page) withContext(Dispatchers.IO) { msg.editMessage(embed).complete() } else msg
        } else withContext(Dispatchers.IO) { ctx.channel.sendMessage(embed).complete() }
        this@EmbedReactionMenu.msg = m
        try {
            listen(m, ctx)
        } catch (ignored: TimeoutException) {
        }

        if (first) {
            with(ctx) {
                launch {
                    try {
                        m?.react(reactions.stop)

                        if (embeds.size > 1) {
                            m?.react(reactions.first)
                            m?.react(reactions.previous)
                            m?.react(reactions.next)
                            m?.react(reactions.last)
                        }
                    } catch (e: ContextException) {
                    }
                }
            }
        }
    }

    private fun listen(message: Message, ctx: CommandProcessingContext) {
        message.on<MessageReactionAddEvent>()
            .filter { it.user != ctx.event.jda.selfUser }
            .timeout(timeout)
            .doOnError { message.clearReactions().queue({}, {}) }
            .next()
            .subscribe {
                try {
                    it.reaction.removeReaction(it.user!!).queue({}, {})
                } catch (e: Exception) {
                }

                if (it.user == ctx.executor)
                    when (it.reactionEmote.name) {
                        reactions.stop -> {
                            listener?.dispose()
                            if (delete) message.delete().queue({}, {})
                            else message.clearReactions().queue({}, {})
                        }
                        reactions.first -> launch { sendTo(ctx, 0) }
                        reactions.previous -> launch { sendTo(ctx, max(index - 1, 0)) }
                        reactions.next -> launch { sendTo(ctx, min(index + 1, embeds.size - 1)) }
                        reactions.last -> launch { sendTo(ctx, embeds.size - 1) }
                    }
                else
                    launch { listen(message, ctx) }
            }
    }
}
