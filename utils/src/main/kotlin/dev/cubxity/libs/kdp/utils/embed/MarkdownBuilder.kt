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

package dev.cubxity.libs.kdp.utils.embed

import dev.cubxity.libs.kdp.utils.BuilderTagMarker
import dev.cubxity.libs.kdp.utils.escapeMarkdown
import net.dv8tion.jda.api.entities.*

/**
 * @author Cubxity
 * @since 6/10/2019
 */
class MarkdownBuilder : MDString {
    private val builder = StringBuilder()

    operator fun String.unaryPlus() {
        builder.append(escapeMarkdown())
    }

    operator fun MDString.unaryPlus() {
        builder.append(toString())
    }

    operator fun User.unaryPlus() {
        builder.append(asMention)
    }

    operator fun Member.unaryPlus() {
        builder.append(asMention)
    }

    operator fun Role.unaryPlus() {
        builder.append(asMention)
    }

    operator fun Emote.unaryPlus() {
        builder.append(asMention)
    }

    operator fun TextChannel.unaryPlus() {
        builder.append(asMention)
    }

    /**
     * Appends [s] into [builder]
     * @return this instance for chaining
     */
    fun append(s: String): MarkdownBuilder {
        builder.append(s.escapeMarkdown())
        return this
    }

    /**
     * Appends [s] into [builder] without escaping
     * @return this instance for chaining
     */
    fun unsafe(s: String): MarkdownBuilder {
        builder.append(s)
        return this
    }

    /**
     * Returns [builder]
     */
    override fun toString() = builder.toString()
}

/**
 * String that has been already escaped
 */
interface MDString

class SimpleMDString(private val s: String) : MDString {
    override fun toString() = s
}

fun String.asMDString() = SimpleMDString(this)

/**
 * @param opt callback to configure [MarkdownBuilder]
 */
@BuilderTagMarker
fun markdown(opt: MarkdownBuilder.() -> Unit = {}) =
    MarkdownBuilder().apply(opt)