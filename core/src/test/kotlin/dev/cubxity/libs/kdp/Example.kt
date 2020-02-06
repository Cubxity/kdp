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

import dev.cubxity.libs.kdp.dsl.command
import dev.cubxity.libs.kdp.dsl.sub
import dev.cubxity.libs.kdp.module.Module
import dev.cubxity.libs.kdp.module.ModuleInitializer
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline
import dev.cubxity.libs.kdp.processing.processing
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDABuilder
import java.awt.Color

class ExampleModule(kdp: KDP) : Module(kdp, "example") {
    companion object : ModuleInitializer<ExampleModule>({ ExampleModule(it) }) {
        val example by command(description = "Example command")
        val error by example.sub(description = "Throws an error")
        val not by error.sub(description = "It does not throw an error")
    }

    init {
        example {
            handler { send("Hello world!") }
        }
        error {
            handler { throw error("Omega lul") }
        }
        not {
            handler { send("You are safe.") }
        }
    }
}

fun main() {
    val kdp = kdp {
        ExampleModule.register()

        processing {
            prefix = "^"
        }

        intercept(CommandProcessingPipeline.ERROR) {
            val embed = EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Error")
                .setDescription(context.exception?.toString())
                .build()
            context.send(embed)
        }

        init()
    }
    JDABuilder()
        .setEventManager(kdp.manager)
        .setToken(System.getProperty("token"))
        .build()
}