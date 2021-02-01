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

package dev.cubxity.kdp.engines.jda.entity

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.entity.Overwrite
import dev.cubxity.kdp.entity.Permissions
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.channel.OverwriteType
import net.dv8tion.jda.api.entities.PermissionOverride

class JDAOverwrite(override val kdp: KDP, private val overwrite: PermissionOverride) : Overwrite {
    override val id: Snowflake
        get() = overwrite.snowflake

    override val type: OverwriteType
        get() = if (overwrite.isMemberOverride) OverwriteType.Member else OverwriteType.Role

    override val allow: Permissions
        get() = Permissions(overwrite.allowedRaw)

    override val deny: Permissions
        get() = Permissions(overwrite.deniedRaw)
}