/*
 *  KDP is a modular and customizable Discord command processing library.
 *  Copyright (C) 2020 Cubxity.
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

package dev.cubxity.kdp.gateway

/**
 * Values that enable a group of gateway events.
 * See: https://discord.com/developers/docs/topics/gateway#gateway-intents.
 */
enum class Intent(val code: Int) {
    Guild(1 shl 0),
    GuildMembers(1 shl 1),
    GuildBans(1 shl 2),
    GuildEmojis(1 shl 3),
    GuildIntegrations(1 shl 4),
    GuildWebhooks(1 shl 5),
    GuildInvites(1 shl 6),
    GuildVoiceStates(1 shl 7),
    GuildPresences(1 shl 8),
    GuildMessages(1 shl 9),
    GuildMessageReactions(1 shl 10),
    GuildMessageTyping(1 shl 11),
    DirectMessages(1 shl 12),
    DirectMessageReactions(1 shl 13),
    DirectMessageTyping(1 shl 14)
}

/**
 * A set of [intents][Intent] to be used while connection to communicate the events the client wishes to receive.
 */
inline class Intents(val code: Int) {
    /**
     * List of [intents][Intent] that are enabled.
     */
    val intents: List<Intent>
        get() = Intent.values().filter { it in this }

    operator fun contains(intent: Intent): Boolean =
        code and intent.code != 0

    operator fun plus(intent: Intent): Intents =
        Intents(code or intent.code)

    operator fun plus(intents: Intents): Intents =
        Intents(code or intents.code)

    operator fun minus(intent: Intent): Intents =
        Intents(code xor intent.code)

    operator fun minus(intents: Intents): Intents =
        Intents(code xor intents.code)

    companion object {
        inline val all: Intents
            get() = invoke {
                Intent.values().forEach { +it }
            }

        inline val nonPrivileged: Intents
            get() = invoke {
                Intent.values().forEach { +it }

                -Intent.GuildPresences
                -Intent.GuildMembers
            }

        inline val none: Intents
            get() = Intents(0)

        inline operator fun invoke(builder: Builder.() -> Unit = {}): Intents =
            Builder().apply(builder).build()
    }

    class Builder(private var intents: Intents = none) {
        operator fun Intent.unaryPlus() {
            this@Builder.intents = this@Builder.intents + this
        }

        operator fun Intents.unaryPlus() {
            this@Builder.intents = this@Builder.intents + this
        }

        operator fun Intent.unaryMinus() {
            this@Builder.intents = this@Builder.intents - this
        }

        operator fun Intents.unaryMinus() {
            this@Builder.intents = this@Builder.intents - this
        }

        fun build(): Intents = intents
    }
}