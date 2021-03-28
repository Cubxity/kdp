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

package dev.cubxity.kdp.engine.jda.entity.channel

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.engine.jda.entity.JDAGuild
import dev.cubxity.kdp.engine.jda.entity.JDAOverwrite
import dev.cubxity.kdp.engine.jda.entity.snowflake
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.channel.ChannelType
import dev.cubxity.kdp.entity.channel.GuildChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.entities.ChannelType as JDAChannelType
import net.dv8tion.jda.api.entities.GuildChannel as JDAGuildChannel

@KDPUnsafe
interface JDAGuildChannel : GuildChannel {
    val channel: JDAGuildChannel

    override val id: Snowflake
        get() = channel.snowflake

    override val type: ChannelType
        get() = mapType()

    override val guild: JDAGuild
        get() = JDAGuild(channel.guild)

    override val position: Int
        get() = channel.positionRaw

    override val permissionOverwrites: List<JDAOverwrite>
        get() = channel.permissionOverrides.map { JDAOverwrite(it) }

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

    override val parent: GuildChannel?
        get() = null // TODO

    override val lastPinTimestamp: String?
        get() = null // Not supported

    private fun mapType(): ChannelType = when (channel.type) {
        JDAChannelType.TEXT -> ChannelType.GuildText
        JDAChannelType.PRIVATE -> ChannelType.DM
        JDAChannelType.VOICE -> ChannelType.GuildVoice
        JDAChannelType.GROUP -> ChannelType.GroupDm
        JDAChannelType.CATEGORY -> ChannelType.GuildCategory
        JDAChannelType.STORE -> ChannelType.GuildStore
        JDAChannelType.UNKNOWN -> ChannelType.Unknown(-1)
    }
}