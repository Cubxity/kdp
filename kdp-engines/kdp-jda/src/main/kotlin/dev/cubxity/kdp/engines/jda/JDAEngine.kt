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

package dev.cubxity.kdp.engines.jda

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.engine.BaseKDPEngine
import dev.cubxity.kdp.engine.KDPEngineEnvironment
import dev.cubxity.kdp.gateway.Intent
import dev.cubxity.kdp.kdp
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

class JDAEngine(
    environment: KDPEngineEnvironment<JDAEngine>,
    configure: Configuration.() -> Unit
) : BaseKDPEngine<JDAEngine>(environment) {
    class Configuration : BaseKDPEngine.Configuration()

    private val configuration = Configuration().apply(configure)
    private var jda: JDA? = null

    @KDPUnsafe
    override val unsafe: JDA
        get() = jda ?: error("JDA has not been started")

    override suspend fun login(await: Boolean): JDAEngine {
        val jda = JDABuilder.create(environment.token, mapIntents()).build()
        this.jda = jda

        if (await) {
            if (jda.status === JDA.Status.CONNECTED) return this
            while (!jda.status.isInit || jda.status < JDA.Status.CONNECTED) {
                if (jda.status === JDA.Status.SHUTDOWN) {
                    error("JDA was shutdown trying to await status")
                }

                delay(50)
            }
        }

        return this
    }

    override suspend fun shutdown() {
        jda?.shutdownNow()
        jda = null
    }

    private fun mapIntents(): List<GatewayIntent> = configuration.intents.intents.mapNotNull {
        when (it) {
            Intent.Guild -> null // Not supported
            Intent.GuildMembers -> GatewayIntent.GUILD_MEMBERS
            Intent.GuildBans -> GatewayIntent.GUILD_BANS
            Intent.GuildEmoji -> GatewayIntent.GUILD_EMOJIS
            Intent.GuildIntegrations, Intent.GuildWebhooks -> null // Unused
            Intent.GuildInvites -> GatewayIntent.GUILD_INVITES
            Intent.GuildVoiceStates -> GatewayIntent.GUILD_VOICE_STATES
            Intent.GuildPresences -> GatewayIntent.GUILD_PRESENCES
            Intent.GuildMessages -> GatewayIntent.GUILD_MESSAGES
            Intent.GuildMessageReactions -> GatewayIntent.GUILD_MESSAGE_REACTIONS
            Intent.GuildMessageTyping -> GatewayIntent.GUILD_MESSAGE_TYPING
            Intent.DirectMessages -> GatewayIntent.DIRECT_MESSAGES
            Intent.DirectMessageReactions -> GatewayIntent.DIRECT_MESSAGE_REACTIONS
            Intent.DirectMessageTyping -> GatewayIntent.DIRECT_MESSAGE_TYPING
        }
    }
}

suspend fun main() {
    kdp(JDA, "Joe") {
        // Your logic here
    }.login()
}