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
import dev.cubxity.libs.kdp.command.SubCommand
import dev.cubxity.libs.kdp.command.SubCommandData
import reactor.core.Disposable
import kotlin.math.abs

/**
 * Represents a module. A module may contain commands
 *
 * @property name the name of the module, could be used in help menus and such.
 * @property description the description of the module, could be used in help menus and such.
 */
open class Module(val kdp: KDP, val name: String, val description: String = "No description provided.") {
    val commands = mutableListOf<Command>()
    val listeners = mutableListOf<Disposable>()

    /**
     * Operator function to construct the command or sub command command and configure it.
     */
    inline operator fun <T : CommandData> T.invoke(opt: Command.() -> Unit) =
        if (this is SubCommandData) {
            var absoluteParent: CommandData = parent
            val subCommands = mutableListOf<SubCommandData>()
            while (true) {
                if (absoluteParent is SubCommandData) {
                    subCommands += absoluteParent
                    absoluteParent = absoluteParent.parent
                } else break
            }
            subCommands.reverse() // Since it's going bottom up

            val cmd = commands.find { it.name == absoluteParent.name } ?: parent.build().also { commands += it }
            var currentSub: SubCommand? = null
            subCommands.forEach {
                val effectiveCommand = currentSub ?: cmd
                currentSub = cmd.subCommands.find { sc -> sc.name == it.name }
                    ?: it.build().also { sc -> effectiveCommand.subCommands += sc }
            }
            val effectiveCommand = currentSub ?: cmd
            val sc = cmd.subCommands.find { it.name == name } ?: build().also { effectiveCommand.subCommands += it }
            opt(sc)
            cmd
        } else {
            val cmd = commands.find { it.name == name } ?: build().also { commands += it }
            opt(cmd)
            cmd
        }

    open suspend fun dispose() {
        commands.clear()
        listeners.forEach { it.dispose() }
        listeners.clear()
    }
}