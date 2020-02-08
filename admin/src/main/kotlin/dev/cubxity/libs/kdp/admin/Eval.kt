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

import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.utils.embed.embed
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import javax.script.Bindings

private val engine = KotlinJsr223JvmLocalScriptEngineFactory().scriptEngine

fun AdminModule.eval() = AdminModule.eval {
    botAdmin = true
    handler {
        val code: String = args["code"]!!
        val bindings = engine.createBindings()
        bindings["ctx"] = this
        bindings["kdp"] = kdp
        bindings["g"] = guild
        bindings["u"] = executor
        bindings["m"] = message
        bindings["c"] = channel

        sendTyping()
        val header = createHeader(bindings)
        val out = engine.eval("$header$code", bindings)

        val embed = embed {
            title = "Output"
            +out.toString()
        }
        send(embed)
    }
}

private fun createHeader(bindings: Bindings) = buildString {
    bindings.forEach { (k) ->
        if (k != "kotlin.script.engine") append("val $k=bindings[\"$k\"]\n")
    }
}