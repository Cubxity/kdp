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

package dev.cubxity.libs.kdp

import dev.cubxity.libs.kdp.command.CommandStringParser
import dev.cubxity.libs.kdp.dsl.command
import dev.cubxity.libs.kdp.dsl.sub
import dev.cubxity.libs.kdp.module.Module
import dev.cubxity.libs.kdp.module.ModuleInitializer
import net.dv8tion.jda.api.JDABuilder

class ExampleModule : Module("example") {
    companion object : ModuleInitializer<ExampleModule>({ ExampleModule() }) {
        val help by command(description = "Show help menu")
        val cmdHelp by help.sub("cmd", "Show help for a command")
    }

    init {
        CommandStringParser.parse("c|a")
        help {

        }
        cmdHelp {

        }
    }
}

fun main() {
    val kdp = kdp {
        ExampleModule.register()
    }
    JDABuilder()
        .setEventManager(kdp.manager)
        .setToken(System.getProperty("token"))
        .build()
}