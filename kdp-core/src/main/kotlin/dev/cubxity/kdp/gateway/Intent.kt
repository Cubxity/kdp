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
    GUILD(1 shl 0),
    GUILD_MEMBERS(1 shl 1),
    GUILD_BANS(1 shl 2),
    GUILD_EMOJI(1 shl 3),
    GUILD_INTEGRATIONS(1 shl 4),
    GUILD_WEBHOOKS(1 shl 5),
    GUILD_INVITES(1 shl 6),
    GUILD_VOICE_STATES(1 shl 7),
    GUILD_PRESENCES(1 shl 8),
    GUILD_MESSAGES(1 shl 9),
    GUILD_MESSAGE_REACTIONS(1 shl 10),
    GUILD_MESSAGE_TYPING(1 shl 11),
    DIRECT_MESSAGES(1 shl 12),
    DIRECT_MESSAGE_REACTIONS(1 shl 13),
    DIRECT_MESSAGE_TYPING(1 shl 14)
}

/**
 * A set of [intents][Intent] to be used while connection to communicate the events the client wishes to receive.
 */
inline class Intents(val code: Int) {
    /**
     * List of [intents][Intents] that are enabled.
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

                -Intent.GUILD_PRESENCES
                -Intent.GUILD_MEMBERS
            }

        inline val none: Intents
            get() = invoke()

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