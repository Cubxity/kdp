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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.dv8tion.jda.api.entities.*

@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
class CommandProcessingContext(
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
     * The alias used on invocation
     */
    val alias: String,

    /**
     * The parsed arguments, this may not match with [message]
     */
    val args: Array<String>
) {
    val guild: Guild?
        get() = (channel as? TextChannel)?.guild

    /**
     * Sends [message] to [channel]
     */
    suspend fun send(message: String): Message = withContext(Dispatchers.IO) {
        channel.sendMessage(message).complete()
    }

    /**
     * Sends [message] to [channel]
     */
    suspend fun send(message: MessageEmbed): Message = withContext(Dispatchers.IO) {
        channel.sendMessage(message).complete()
    }

    /**
     * React with [emote] on [message]
     */
    suspend fun react(emote: Emote) {
        withContext(Dispatchers.IO) { message.addReaction(emote).complete() }
    }

    /**
     * React with [unicode] on [message]
     */
    suspend fun react(unicode: String) {
        withContext(Dispatchers.IO) { message.addReaction(unicode).complete() }
    }

    /**
     * Deletes [message]
     */
    suspend fun delete() {
        withContext(Dispatchers.IO) { message.delete().complete() }
    }
}