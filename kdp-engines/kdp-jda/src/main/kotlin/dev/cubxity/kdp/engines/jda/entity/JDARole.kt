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

package dev.cubxity.kdp.engines.jda.entity

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.engines.jda.JDAEngine
import dev.cubxity.kdp.entity.Permissions
import dev.cubxity.kdp.entity.Snowflake
import net.dv8tion.jda.api.entities.Role
import dev.cubxity.kdp.entity.Role as KDPRole

class JDARole(override val kdp: KDP<JDAEngine>, private val role: Role) : KDPRole<JDAEngine> {
    override val id: Snowflake
        get() = role.snowflake

    override val name: String
        get() = role.name

    override val color: Int
        get() = role.colorRaw

    override val isHoist: Boolean
        get() = role.isHoisted

    override val position: Int
        get() = role.positionRaw

    override val permissions: Permissions
        get() = Permissions(role.permissionsRaw)

    override val isManaged: Boolean
        get() = role.isManaged

    override val isMentionable: Boolean
        get() = role.isMentionable
}