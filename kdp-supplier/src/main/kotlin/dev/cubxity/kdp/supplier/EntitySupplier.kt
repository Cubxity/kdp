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

package dev.cubxity.kdp.supplier

import dev.cubxity.kdp.entity.*
import dev.cubxity.kdp.entity.channel.Channel
import dev.cubxity.kdp.entity.channel.GuildChannel
import dev.cubxity.kdp.exception.*
import kotlinx.coroutines.flow.Flow

interface EntitySupplier {
    val guilds: Flow<Guild>

    suspend fun getGuildOrNull(id: Snowflake): Guild?

    suspend fun getGuild(id: Snowflake): Guild =
        getGuildOrNull(id) ?: guildNotFound(id)

    suspend fun getChannelOrNull(id: Snowflake): Channel?

    suspend fun getChannel(id: Snowflake): Channel =
        getChannelOrNull(id) ?: channelNotFound(id)

    fun getGuildChannels(guildId: Snowflake): Flow<GuildChannel>

    fun getChannelPins(channelId: Snowflake): Flow<Message>

    suspend fun getMemberOrNull(guildId: Snowflake, userId: Snowflake): Member?

    suspend fun getMember(guildId: Snowflake, userId: Snowflake): Member =
        getMemberOrNull(guildId, userId) ?: memberNotFound(guildId, userId)

    suspend fun getMessageOrNull(channelId: Snowflake, messageId: Snowflake): Message?

    suspend fun getMessage(channelId: Snowflake, messageId: Snowflake): Message =
        getMessageOrNull(channelId, messageId) ?: messageNotFound(channelId, messageId)

    fun getMessages(channelId: Snowflake, position: Position, limit: Int = Int.MAX_VALUE): Flow<Message>

    suspend fun getSelfOrNull(): User?

    suspend fun getSelf(): User =
        getSelfOrNull() ?: selfNotFound()

    suspend fun getUserOfNull(id: Snowflake): User?

    suspend fun getUser(id: Snowflake): User =
        getUserOfNull(id) ?: userNotFound(id)

    suspend fun getRoleOrNull(guildId: Snowflake, roleId: Snowflake): Role?

    suspend fun getRole(guildId: Snowflake, roleId: Snowflake): Role =
        getRoleOrNull(guildId, roleId) ?: roleNotFound(guildId, roleId)

    fun getGuildRoles(guildId: Snowflake): Flow<Role>

    // TODO: Guild bans

    fun getGuildMembers(guildId: Snowflake, limit: Int = Int.MAX_VALUE): Flow<Member>

    // TODO: Guild voice regions

    fun getEmojis(guildId: Snowflake): Flow<Emoji>

    // TODO: Guild webhook

    // TODO: Guild invites

    // TODO: Application info

    // TODO: Guild widget

    // TODO: Guild template

    // TODO: Guild audit log
}