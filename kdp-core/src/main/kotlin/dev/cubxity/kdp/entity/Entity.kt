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

package dev.cubxity.kdp.entity

import dev.cubxity.kdp.KDPObject

/**
 * Represents an entity that's identified by it's [id].
 */
interface Entity : KDPObject, Comparable<Entity> {
    /**
     * The unique identifier of this entity.
     */
    val id: Snowflake

    /**
     * Compares entities on [id].
     */
    override operator fun compareTo(other: Entity): Int =
        comparator.compare(this, other)

    companion object {
        private val comparator = compareBy<Entity> { it.id }
    }
}

interface MentionableEntity : Entity {
    val mention: String
}