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

package dev.cubxity.libs.kdp.utils.paginator

import club.minnced.jda.reactor.on
import dev.cubxity.libs.kdp.processing.CommandProcessingContext
import kotlinx.coroutines.*
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import reactor.core.Disposable
import kotlin.math.max
import kotlin.math.min

class PaginatorEmbedInterface(
    paginator: Paginator,
    private val embed: EmbedBuilder = EmbedBuilder(),
    private val reactions: PaginatorReactions = PaginatorReactions()
) : CoroutineScope{
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
        clone.setFooter("Page ${page + 1}/${chunks.size}")
        send(ctx, clone.build())
        index = page
    }

    @Suppress("SuspendFunctionOnCoroutineScope")
    private suspend fun send(ctx: CommandProcessingContext, embed: MessageEmbed)  {
        val msg = msg
        if (msg != null) {
            withContext(Dispatchers.IO) { msg.editMessage(embed).complete() }
        } else {
            // TODO
            // Not sending via ctx.send because I have not implemented ctx.edit
            val m = withContext(Dispatchers.IO) { ctx.channel.sendMessage(embed).complete() }
            this@PaginatorEmbedInterface.msg = m
            if (chunks.size > 1) {
                m.on<MessageReactionAddEvent>()
                    .filter { it.user != ctx.event.jda.selfUser }
                    .subscribe {
                        it.reaction.removeReaction(it.user!!).queue()
                        if (it.user == ctx.executor)
                            when (it.reactionEmote.name) {
                                reactions.stop -> {
                                    listener?.dispose()
                                    m.clearReactions().queue()
                                }
                                reactions.first -> launch { sendTo(ctx, 0) }
                                reactions.previous -> launch { sendTo(ctx, max(index - 1, 0)) }
                                reactions.next -> launch { sendTo(ctx, min(index + 1, chunks.lastIndex)) }
                                reactions.last -> launch { sendTo(ctx, chunks.lastIndex) }
                            }
                    }
                with(ctx) {
                    m.react(reactions.stop)
                    m.react(reactions.first)
                    m.react(reactions.previous)
                    m.react(reactions.next)
                    m.react(reactions.last)
                }
            }
        }
    }
}