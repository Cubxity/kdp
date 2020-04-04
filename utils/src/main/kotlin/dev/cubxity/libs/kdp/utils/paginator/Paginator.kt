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

package dev.cubxity.libs.kdp.utils.paginator

import dev.cubxity.libs.kdp.utils.BuilderTagMarker
import dev.cubxity.libs.kdp.utils.escapeMarkdown
import java.lang.StringBuilder

class Paginator(private val prefix: String = "", private val suffix: String = "", private val size: Int = 2000) {
    private val lines = mutableListOf<String>()
    val chunks: List<String>
        get() {
            val l = mutableListOf<String>()
            val ex = prefix.length + suffix.length
            var chunk = StringBuilder()
            lines.forEach {
                val n = "$chunk\n$it"
                if (n.length + ex <= size) {
                    chunk.append(it)
                } else {
                    l += chunk.toString()
                    chunk = StringBuilder().append(it)
                }
            }
            val c = chunk.toString()
            if (c.isNotEmpty()) l += c
            return l
        }

    operator fun String.unaryPlus() {
        val s = escapeMarkdown()
        if (s.length + prefix.length + suffix.length > size) error("This line is too long! (${s.length})")
        lines += s
    }

    fun addLine(line: String, escape: Boolean = true) {
        val s = if (escape) line.escapeMarkdown() else line
        if (s.length + prefix.length + suffix.length > size) error("This line is too long! (${s.length})")
        lines += s
    }
}

@BuilderTagMarker
fun paginator(opt: Paginator.() -> Unit) = Paginator().apply(opt)