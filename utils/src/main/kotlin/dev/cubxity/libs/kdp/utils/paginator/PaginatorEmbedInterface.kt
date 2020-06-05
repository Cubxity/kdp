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
import dev.cubxity.libs.kdp.utils.await
import kotlinx.coroutines.*
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.exceptions.ContextException
import reactor.core.Disposable
import java.time.Duration
import java.util.concurrent.TimeoutException
import kotlin.math.max
import kotlin.math.min

@Suppress("UNUSED")
class PaginatorEmbedInterface(
    paginator: Paginator,
    private val embed: EmbedBuilder = EmbedBuilder(),
    private val reactions: PaginatorReactions = PaginatorReactions(),
    private val footer: String? = null,
    private val delete: Boolean = true,
    private val timeout: Duration = Duration.ofSeconds(15),
    private val editMessage: Message? = null,
    private val clearContent: Boolean = true
) : CoroutineScope {
    override val coroutineContext = Dispatchers.Default + Job()
    private val chunks = paginator.chunks
    private var listener: Disposable? = null
    private var index = 0
    private var msg: Message? = null

    /**
     * @param page page index
     */
    suspend fun sendTo(ctx: CommandProcessingContext, page: Int = 0) {
        val clone = EmbedBuilder(embed)
        clone.setDescription(chunks[page])
        clone.setFooter("Page ${page + 1}/${chunks.size}${if (footer == null) "" else " | $footer"}")
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
            if (first || index != page) msg.editMessage(embed).await() else msg
        } else {
            ctx.channel.sendMessage(embed).await()
        }
        this@PaginatorEmbedInterface.msg = m
        try {
            listen(m, ctx)
        } catch (ignored: TimeoutException) {
        }

        if (first) {
            with(ctx) {
                launch {
                    try {
                        m?.addReaction(reactions.stop)?.await()

                        if (chunks.size > 1) {
                            m?.addReaction(reactions.first)?.await()
                            m?.addReaction(reactions.previous)?.await()
                            m?.addReaction(reactions.next)?.await()
                            m?.addReaction(reactions.last)?.await()
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
                            if (delete) message.delete().queue()
                            else message.clearReactions().queue({}, {})
                        }
                        reactions.first -> launch { sendTo(ctx, 0) }
                        reactions.previous -> launch { sendTo(ctx, max(index - 1, 0)) }
                        reactions.next -> launch { sendTo(ctx, min(index + 1, chunks.size - 1)) }
                        reactions.last -> launch { sendTo(ctx, chunks.size - 1) }
                    }
                else
                    launch { listen(message, ctx) }
            }
    }
}
