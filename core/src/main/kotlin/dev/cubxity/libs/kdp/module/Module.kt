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

package dev.cubxity.libs.kdp.module

import dev.cubxity.libs.kdp.KDP
import dev.cubxity.libs.kdp.command.Command
import dev.cubxity.libs.kdp.command.CommandData
import dev.cubxity.libs.kdp.command.SubCommandData

/**
 * Represents a module. A module may contain commands
 *
 * @property name the name of the module, could be used in help menus and such.
 * @property description the description of the module, could be used in help menus and such.
 */
open class Module(val kdp: KDP, val name: String, val description: String = "No description provided.") {
    val commands = mutableListOf<Command>()

    /**
     * Operator function to construct the command or sub command command and configure it.
     */
    inline operator fun <T : CommandData> T.invoke(opt: T.() -> Unit) =
        if (this is SubCommandData) {
            val cmd = commands.find { it.name == root.name } ?: root.build().also { commands += it }
            val subCommand = cmd.subCommands.find { it.name == name } ?: build().also { cmd.subCommands += it }
            opt(subCommand as T)
            cmd
        } else {
            val cmd = commands.find { it.name == name } ?: build().also { commands += it }
            opt(cmd as T)
            cmd
        }
}