/*
 *  KDP is a modular and customizable Discord command processing library.
 *  Copyright (C) 2020-2021 Cubxity.
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

package dev.cubxity.kdp.engines.kord

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.engine.BaseKDPEngine
import dev.cubxity.kdp.engine.KDPEngineEnvironment
import dev.cubxity.kdp.gateway.Intent
import dev.kord.core.Kord
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import dev.kord.gateway.Intent as KordIntent

class KordEngine(
    environment: KDPEngineEnvironment<KordEngine>,
    configure: Configuration.() -> Unit
) : BaseKDPEngine<KordEngine>(environment) {
    class Configuration : BaseKDPEngine.Configuration()

    private val configuration = Configuration().apply(configure)
    private var kord: Kord? = null

    @KDPUnsafe
    override val unsafe: Kord
        get() = kord ?: error("Kord has not been started")

    override suspend fun login(await: Boolean): KordEngine {
        val kord = Kord(environment.token) {
            intents = mapIntents()
        }
        this.kord = kord

        kord.login()

        return this
    }

    override suspend fun shutdown() {
        kord?.shutdown()
        kord = null
    }

    @OptIn(PrivilegedIntent::class)
    private fun mapIntents(): Intents = Intents {
        configuration.intents.intents.forEach {
            when (it) {
                Intent.GuildMembers -> +KordIntent.GuildMembers
                Intent.GuildBans -> +KordIntent.GuildBans
                Intent.GuildEmojis -> +KordIntent.GuildEmojis
                Intent.GuildIntegrations -> +KordIntent.GuildIntegrations
                Intent.GuildWebhooks -> +KordIntent.GuildWebhooks
                Intent.GuildInvites -> +KordIntent.GuildInvites
                Intent.GuildVoiceStates -> +KordIntent.GuildVoiceStates
                Intent.GuildPresences -> +KordIntent.GuildPresences
                Intent.GuildMessages -> +KordIntent.GuildMessages
                Intent.GuildMessageReactions -> +KordIntent.GuildMessageReactions
                Intent.GuildMessageTyping -> +KordIntent.GuildMessageTyping
                Intent.DirectMessages -> +KordIntent.DirectMessages
                Intent.DirectMessageReactions -> +KordIntent.DirectMessagesReactions
                Intent.DirectMessageTyping -> +KordIntent.DirectMessageTyping
            }
        }
    }
}