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

@file:Suppress("NOTHING_TO_INLINE")

package dev.cubxity.kdp.exception

import dev.cubxity.kdp.entity.Snowflake

class EntityNotFoundException(message: String) : IllegalStateException(message)

inline fun userNotFound(userId: Snowflake): Nothing =
    entityNotFound("User", userId)

inline fun selfNotFound(): Nothing =
    throw EntityNotFoundException("Self user was not found")

inline fun guildNotFound(guildId: Snowflake): Nothing =
    entityNotFound("Guild", guildId)

inline fun channelNotFound(channelId: Snowflake): Nothing =
    entityNotFound("Channel", channelId)

inline fun memberNotFound(guildId: Snowflake, userId: Snowflake): Nothing =
    guildEntityNotFound("Member", guildId, userId)

inline fun roleNotFound(guildId: Snowflake, roleId: Snowflake): Nothing =
    guildEntityNotFound("Role", guildId, roleId)

inline fun messageNotFound(channelId: Snowflake, messageId: Snowflake): Nothing =
    throw EntityNotFoundException("Message with id $messageId in channel $channelId was not found")


@PublishedApi
internal inline fun entityNotFound(entityType: String, id: Snowflake): Nothing =
    throw EntityNotFoundException("$entityType with id $id was not found.")

@PublishedApi
internal inline fun guildEntityNotFound(entityType: String, guildId: Snowflake, id: Snowflake): Nothing =
    throw EntityNotFoundException("$entityType with id $id in guild $guildId was not found.")