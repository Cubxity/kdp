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

package dev.cubxity.kdp.engines.jda

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.engine.BaseKDPEngine
import dev.cubxity.kdp.engine.KDPEngineEnvironment
import dev.cubxity.kdp.engines.jda.event.JDARawEvent
import dev.cubxity.kdp.engines.jda.gateway.JDAGateway
import dev.cubxity.kdp.gateway.Intent
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.requests.GatewayIntent

@OptIn(KDPUnsafe::class)
class JDAEngine(
    environment: KDPEngineEnvironment,
    configure: Configuration.() -> Unit
) : BaseKDPEngine(environment) {
    class Configuration : BaseKDPEngine.Configuration()

    private val configuration = Configuration().apply(configure)
    private var jda: JDA? = null

    override val gateway: JDAGateway = JDAGateway(this)

    @KDPUnsafe
    override val unsafe: JDA
        get() = jda ?: error("JDA has not been started")

    override suspend fun login() {
        val jda = JDABuilder.create(environment.token, mapIntents())
            .setEventManager(gateway)
            .build()
        this.jda = jda

        gateway.events.filterIsInstance<JDARawEvent>()
            .filter { it.event is ShutdownEvent }
            .first()
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
            Intent.GuildEmojis -> GatewayIntent.GUILD_EMOJIS
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