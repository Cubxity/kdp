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

package dev.cubxity.kdp.engines.jda.event.message

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.engines.jda.JDAEngine
import dev.cubxity.kdp.engines.jda.entity.JDAGuild
import dev.cubxity.kdp.engines.jda.entity.JDAMember
import dev.cubxity.kdp.engines.jda.entity.JDAMessage
import dev.cubxity.kdp.engines.jda.entity.snowflake
import dev.cubxity.kdp.engines.jda.event.shard
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.event.message.MessageCreateEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class JDAGuildMessageReceivedEvent(
    override val kdp: KDP<JDAEngine>,
    private val event: GuildMessageReceivedEvent
) : MessageCreateEvent<JDAEngine> {
    override val shard: Int
        get() = event.shard

    override val message: JDAMessage
        get() = JDAMessage(kdp, event.message)

    override val guildId: Snowflake
        get() = event.guild.snowflake

    override val guild: JDAGuild
        get() = JDAGuild(kdp, event.guild)

    override val member: JDAMember?
        get() = event.member?.let { JDAMember(kdp, it) }

    override suspend fun getGuild(): JDAGuild = guild
}