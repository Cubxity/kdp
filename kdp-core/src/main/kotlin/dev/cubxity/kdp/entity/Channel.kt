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

package dev.cubxity.kdp.entity

import dev.cubxity.kdp.engine.KDPEngine

interface Channel<TEngine : KDPEngine<TEngine>> : Entity<TEngine> {
    val type: ChannelType

    val guildId: Snowflake?

    val position: Int?

    val permissionOverwrites: List<Overwrite<TEngine>>?

    val name: String?

    val topic: String?

    val isNsfw: Boolean?

    val lastMessageId: Snowflake?

    val bitrate: Int?

    val userLimit: Int?

    val rateLimitPerUser: Int?

    val recipients: List<User<TEngine>>?

    val icon: Icon<TEngine>?

    val ownerId: Snowflake?

    val applicationId: Snowflake?

    val parentId: Snowflake?

    val lastPinTimestamp: String?
}

enum class ChannelType(val code: Int) {
    GuildText(0),
    DM(1),
    GuildVoice(2),
    GroupDm(3),
    GuildCategory(4),
    GuildNews(5),
    GuildStore(6)
}

enum class OverwriteType(val code: Int) {
    Role(0),
    Member(1)
}
