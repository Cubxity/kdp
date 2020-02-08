/*
 *     KDP is a modular and customizable Discord command processing library.
 *     Copyright (C) 2020  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.KDP
import dev.cubxity.libs.kdp.dsl.command
import dev.cubxity.libs.kdp.dsl.sub
import dev.cubxity.libs.kdp.module.Module
import dev.cubxity.libs.kdp.utils.embed.bold
import dev.cubxity.libs.kdp.utils.embed.code
import dev.cubxity.libs.kdp.utils.embed.embed

class AdminModule(kdp: KDP) : Module(kdp, "admin") {
    companion object {
        val admin by command(description = "The root command for admin module")
        val su by admin.sub("su <user> <command...>", "Runs a command on a specific user/member")
    }

    init {
        admin {
            handler {
                val embed = embed {
                    title = "Admin module"
                    +"Available commands:\n".bold
                    +commands.map { it.name.code }
                }
                send(embed)
            }
        }
        su()
    }
}