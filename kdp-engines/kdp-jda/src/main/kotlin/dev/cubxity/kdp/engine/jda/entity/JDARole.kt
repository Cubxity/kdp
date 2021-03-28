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

package dev.cubxity.kdp.engine.jda.entity

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.behavior.GuildBehavior
import dev.cubxity.kdp.entity.Permissions
import dev.cubxity.kdp.entity.Role
import dev.cubxity.kdp.entity.Snowflake
import net.dv8tion.jda.api.entities.Role as JDARole

@KDPUnsafe
inline class JDARole(private val role: JDARole) : Role {
    override val id: Snowflake
        get() = role.snowflake

    override val guild: GuildBehavior
        get() = JDAGuild(role.guild)

    override val name: String
        get() = role.name

    override val color: Int
        get() = role.colorRaw

    override val isHoist: Boolean
        get() = role.isHoisted

    override val position: Int
        get() = role.position

    override val permissions: Permissions
        get() = Permissions(role.permissionsRaw)

    override val isManaged: Boolean
        get() = role.isManaged

    override val isMentionable: Boolean
        get() = role.isMentionable
}