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

import dev.cubxity.libs.kdp.command.SubCommand
import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.processing.CommandProcessingContext
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline
import dev.cubxity.libs.kdp.processing.MissingArgumentException
import net.dv8tion.jda.api.entities.User

private val ARGS_REGEX = "(\"([^\"]+)\"|[^ ]+)( ?)".toRegex()

fun AdminModule.su() = AdminModule.su {
    botAdmin = true
    handler {
        val user: User = args["user"] ?: error("User not found")
        val command: String = args["command"]!!

        val ctx = createContext(user, command)
        kdp.execute(
            ctx,
            CommandProcessingPipeline.POST_FILTER,
            CommandProcessingPipeline.MONITORING,
            CommandProcessingPipeline.PROCESS
        )
    }
}

fun CommandProcessingContext.createContext(user: User, command: String): CommandProcessingContext {
    val ctx = CommandProcessingContext(kdp, user, channel, message, event)
    var args = processArguments(command)

    if (args.isEmpty()) error("Parsed arguments is empty")

    val cmdName = args[0]
    val cmd = kdp.moduleManager.modules.mapNotNull { it.commands.find { c -> cmdName in c.aliases } }
        .firstOrNull() ?: error("Command not found")

    ctx.alias = cmdName
    args = args.subList(1, args.size)

    var subCommand: SubCommand? = null
    var depth = 0
    for (a in args) {
        val c = subCommand ?: cmd
        val sc = c.subCommands.find { a in it.aliases } ?: break
        subCommand = sc
        depth++
    }
    if (depth > 0) args = args.subList(depth, args.size)
    val effectiveCommand = subCommand ?: cmd

    val requiredArgs = effectiveCommand.args?.filter { it.required }
    if (requiredArgs != null && args.size < requiredArgs.size)
        throw MissingArgumentException(requiredArgs[args.size].name)

    ctx.command = effectiveCommand
    ctx.rawArgs = args
    return ctx
}

private fun processArguments(content: String) =
    ARGS_REGEX.findAll(content)
        .mapNotNull {
            it.groupValues.getOrNull(2)?.let { s -> if (s.isEmpty()) it.groupValues.getOrNull(1) else s }
                ?: it.groupValues.getOrNull(1)
        }
        .toList()