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

import dev.cubxity.libs.kdp.KDP
import dev.cubxity.libs.kdp.command.Command
import dev.cubxity.libs.kdp.respond.RespondContext
import kotlinx.coroutines.coroutineScope
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
class CommandProcessingContext(
    val kdp: KDP,

    /**
     * The executor of this context
     */
    val executor: User,

    /**
     * The channel the context originated from
     */
    val channel: MessageChannel,

    /**
     * The message the context originated from,
     * you should NOT parse arguments from this message directly.
     */
    val message: Message,

    /**
     * The event that the context originated from
     */
    val event: GenericMessageEvent
) {
    /**
     * The prefix used on invocation
     */
    var prefix: String? = null

    /**
     * The parsed arguments, this may not match with [message]
     */
    var rawArgs: List<String>? = null

    val args: ArgumentsContainer = ArgumentsContainer(this)

    /**
     * The alias used on invocation
     */
    var alias: String? = null

    /**
     * The matched command
     */
    var command: Command? = null

    /**
     * The guild associated with [message]
     */
    val guild: Guild?
        get() = (channel as? TextChannel)?.guild

    /**
     * Caught [Throwable] under processing
     */
    var exception: Throwable? = null

    /**
     * Sends [message] to [channel]
     */
    suspend fun send(message: String): Message? = send(MessageBuilder(message))

    /**
     * Sends [message] to [channel]
     */
    suspend fun send(message: MessageEmbed): Message? = send(MessageBuilder(message))

    /**
     * Sends [message] to [channel]
     */
    suspend fun send(message: MessageBuilder): Message? = coroutineScope {
        RespondContext(this@CommandProcessingContext, channel, message)
            .also { kdp.respondPipeline.execute(it) }
            .sentMessage
    }

    /**
     * Sends [message] to the receiver
     */
    suspend fun MessageChannel.send(message: String): Message? = send(MessageBuilder(message))

    /**
     * Sends [message] to the receiver
     */
    suspend fun MessageChannel.send(message: MessageEmbed): Message? = send(MessageBuilder(message))

    /**
     * Sends [message] to the receiver
     */
    suspend fun MessageChannel.send(message: MessageBuilder): Message? = coroutineScope {
        RespondContext(this@CommandProcessingContext, channel, message)
            .also { kdp.respondPipeline.execute(it) }
            .sentMessage
    }

    /**
     * React with [emote] on [message]
     * @return true if the reaction was successful, otherwise false
     */
    suspend fun react(emote: Emote): Boolean =
        suspendCoroutine { c -> message.addReaction(emote).queue({ c.resume(true) }, { c.resume(false) }) }

    /**
     * React with [emote] on the receiver
     */
    suspend fun Message.react(emote: Emote): Boolean =
        suspendCoroutine { c -> addReaction(emote).queue({ c.resume(true) }, { c.resume(false) }) }

    /**
     * React with [unicode] on [message]
     */
    suspend fun react(unicode: String): Boolean =
        suspendCoroutine { c -> message.addReaction(unicode).queue({ c.resume(true) }, { c.resume(false) }) }

    /**
     * React with [unicode] on the receiver
     */
    suspend fun Message.react(unicode: String): Boolean =
        suspendCoroutine { c -> addReaction(unicode).queue({ c.resume(true) }, { c.resume(false) }) }

    /**
     * Deletes [message]
     */
    suspend fun delete(): Boolean =
        suspendCoroutine { c -> message.delete().queue({ c.resume(true) }, { c.resume(false) }) }

    /**
     * Deletes the message on the receiver
     */
    suspend fun Message.delete(): Boolean =
        suspendCoroutine { c -> delete().queue({ c.resume(true) }, { c.resume(false) }) }

    /**
     * Send typing signal to [channel]
     */
    suspend fun sendTyping(): Boolean =
        suspendCoroutine { c -> channel.sendTyping().queue({ c.resume(true) }, { c.resume(false) }) }

    @Throws(CommandException::class)
    fun error(message: String): Nothing = throw CommandException(message)
}