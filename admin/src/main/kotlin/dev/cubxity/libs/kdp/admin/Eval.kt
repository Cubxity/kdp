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
import dev.cubxity.libs.kdp.utils.embed.codeBlock
import dev.cubxity.libs.kdp.utils.embed.embed
import org.apache.commons.lang3.exception.ExceptionUtils
import java.awt.Color
import javax.script.ScriptEngineManager
import javax.script.ScriptException

private val engine = ScriptEngineManager().getEngineByExtension("kts")

private val imports = arrayOf(
    "net.dv8tion.jda.api.*",
    "net.dv8tion.jda.api.utils.*",
    "net.dv8tion.jda.internal.entities.*",
    "net.dv8tion.jda.api.entities.*",
    "dev.cubxity.libs.kdp.util.*",
    "dev.cubxity.libs.kdp.utils.*",
    "dev.cubxity.libs.kdp.utils.embed.*",
    "dev.cubxity.libs.kdp.utils.paginator.*",
    "dev.cubxity.libs.kdp.processing.*",
    "dev.cubxity.libs.kdp.*",
    "kotlinx.coroutines.*"
)

fun AdminModule.eval() = AdminModule.eval {
    botAdmin = true
    ignoreQuotes = true

    System.setProperty("idea.use.native.fs.for.win", "false")

    handler {
        val code: String = args["code"]!!
        val bindings = engine.createBindings()
        val header = buildString {
            append(imports.joinToString(separator = "\n") { "import $it" })
            append("\n")

            hashMapOf(
                "ctx" to this@handler,
                "kdp" to kdp,
                "guild" to guild,
                "user" to executor,
                "message" to message,
                "channel" to channel
            ).forEach { (key, value) ->
                bindings[key] = value ?: return@forEach
                append("val $key = bindings[\"$key\"]!! as ${value::class.simpleName}\n")
            }
        }

        sendTyping()
        val start = System.currentTimeMillis()
        val (success, out) = try {
            true to engine.eval("$header$code", bindings)
        } catch (e: Throwable) {
            false to ExceptionUtils.getStackTrace(if (e is ScriptException) e.cause ?: e else e)
        }

        val embed = embed {
            title = "Eval Output"
            color = if (success) Color(102, 187, 106) else Color(239, 83, 80)
            setFooter(
                "Executed in ${System.currentTimeMillis() - start}ms by ${executor.asTag}",
                executor.effectiveAvatarUrl
            )

            +codeBlock("kotlin") { +out.toString() }
        }

        send(embed)
    }
}