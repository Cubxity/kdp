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

package dev.cubxity.kdp.engines.jda.entity.channel

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.engines.jda.JDAEngine
import dev.cubxity.kdp.engines.jda.entity.JDAGuild
import dev.cubxity.kdp.engines.jda.entity.JDAOverwrite
import dev.cubxity.kdp.engines.jda.entity.snowflake
import dev.cubxity.kdp.engines.jda.util.await
import dev.cubxity.kdp.entity.Snowflake
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import dev.cubxity.kdp.entity.channel.ChannelType as KDPChannelType
import dev.cubxity.kdp.entity.channel.GuildChannel as KDPGuildChannel

open class JDAGuildChannel(
    override val kdp: KDP<JDAEngine>,
    private val channel: GuildChannel
) : KDPGuildChannel<JDAEngine> {
    override val id: Snowflake
        get() = channel.snowflake

    override val type: KDPChannelType
        get() = mapType()

    override val guildId: Snowflake
        get() = channel.guild.snowflake

    override val guild: JDAGuild
        get() = JDAGuild(kdp, channel.guild)

    override val position: Int
        get() = channel.positionRaw

    override val permissionOverwrites: List<JDAOverwrite>
        get() = channel.permissionOverrides.map { JDAOverwrite(kdp, it) }

    override val name: String
        get() = channel.name

    override val topic: String?
        get() = (channel as? TextChannel)?.topic

    override val isNSFW: Boolean?
        get() = (channel as? TextChannel)?.isNSFW

    override val lastMessageId: Snowflake?
        get() = null // Not supported

    override val bitrate: Int?
        get() = (channel as? VoiceChannel)?.bitrate

    override val userLimit: Int?
        get() = (channel as? VoiceChannel)?.userLimit

    override val rateLimitPerUser: Int?
        get() = null // Not supported

    override val applicationId: Snowflake?
        get() = null // Not supported

    override val parent: KDPGuildChannel<JDAEngine>?
        get() = channel.parent?.let { JDAGuildChannel(kdp, it) }

    override val lastPinTimestamp: String?
        get() = null // Not supported

    override suspend fun getGuild(): JDAGuild = guild

    override suspend fun getGuildOrNull(): JDAGuild = guild

    override suspend fun delete() {
        channel.delete().await()
    }

    private fun mapType(): KDPChannelType = when (channel.type) {
        ChannelType.TEXT -> KDPChannelType.GuildText
        ChannelType.PRIVATE -> KDPChannelType.DM
        ChannelType.VOICE -> KDPChannelType.GuildVoice
        ChannelType.GROUP -> KDPChannelType.GroupDm
        ChannelType.CATEGORY -> KDPChannelType.GuildCategory
        ChannelType.STORE -> KDPChannelType.GuildStore
        ChannelType.UNKNOWN -> KDPChannelType.Unknown(-1)
    }
}