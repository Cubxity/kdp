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

package dev.cubxity.libs.kdp.command

object CommandSpecParser {
    private val commandStringRegex = "(?<aliases>[^ ]+) ?(?<params>.+)?".toRegex()
    private val paramRegex = "(?<required>[\\[<])(?<name>[^ ]+)[]>] ?".toRegex()

    fun parse(spec: String): Data {
        val res = commandStringRegex.find(spec) ?: error("Unable to parse the command string")
        val aliases = res.groupValues[1].split('|')
        val params = paramRegex.findAll(res.groupValues[2])
            .map {
                val rawName = it.groupValues[2]
                val vararg = rawName.endsWith("...")
                val name = if (vararg) rawName.removeSuffix("...") else rawName
                CommandData.ParameterData(name, it.groupValues[1] == "<", vararg)
            }
            .toList()
        return Data(aliases, params)
    }

    data class Data(val aliases: List<String>, val parameters: List<CommandData.ParameterData>)
}