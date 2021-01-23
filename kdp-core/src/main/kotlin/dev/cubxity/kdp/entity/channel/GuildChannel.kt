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

package dev.cubxity.kdp.entity.channel

import dev.cubxity.kdp.behavior.channel.GuildChannelBehavior
import dev.cubxity.kdp.engine.KDPEngine
import dev.cubxity.kdp.entity.Overwrite
import dev.cubxity.kdp.entity.Snowflake

interface GuildChannel<TEngine : KDPEngine<TEngine>> : Channel<TEngine>, GuildChannelBehavior<TEngine> {
    val position: Int?

    val permissionOverwrites: List<Overwrite<TEngine>>?

    val name: String?

    val topic: String?

    val isNSFW: Boolean?

    val lastMessageId: Snowflake?

    val bitrate: Int?

    val userLimit: Int?

    val rateLimitPerUser: Int?

    val applicationId: Snowflake?

    val parent: GuildChannel<TEngine>?

    val lastPinTimestamp: String?

    override suspend fun asChannel(): GuildChannel<TEngine> = this

    override suspend fun asChannelOrNull(): GuildChannel<TEngine> = this
}

sealed class ChannelType(val value: Int) {
    class Unknown(value: Int) : ChannelType(value)
    object GuildText : ChannelType(0)
    object DM : ChannelType(1)
    object GuildVoice : ChannelType(2)
    object GroupDm : ChannelType(3)
    object GuildCategory : ChannelType(4)
    object GuildNews : ChannelType(5)
    object GuildStore : ChannelType(6)
}

sealed class OverwriteType(val value: Int) {
    class Unknown(value: Int) : OverwriteType(value)
    object Role : OverwriteType(0)
    object Member : OverwriteType(1)
}
