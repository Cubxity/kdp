/*
 * KDP is a modular and customizable Discord command processing library.
 * Copyright (C) 2020 Cubxity.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.utils.embed

import dev.cubxity.libs.kdp.utils.BuilderTagMarker
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.utils.MarkdownSanitizer

/**
 * @author Cubxity
 * @since 6/10/2019
 */
class MarkdownBuilder : MDString {
    private val builder = StringBuilder()

    operator fun String.unaryPlus() {
        builder.append(MarkdownSanitizer.sanitize(this))
    }

    operator fun Any.unaryPlus() {
        +toString()
    }

    operator fun MDString.unaryPlus() {
        builder.append(toString()) // Skip sanitizing
    }

    operator fun IMentionable.unaryPlus() {
        builder.append(asMention)
    }

    /**
     * Appends [s] into [builder]
     * @return this instance for chaining
     */
    fun append(s: String, sanitize: Boolean = false): MarkdownBuilder {
        builder.append(if (sanitize) s else MarkdownSanitizer.sanitize(s))
        return this
    }

    /**
     * Appends [s] into [builder] without escaping
     * @return this instance for chaining
     */
    @Deprecated("Use appendUnsafe", ReplaceWith("append(s, sanitize = true)"))
    fun unsafe(s: String) = append(s, sanitize = true)

    /**
     * Returns [builder]
     */
    override fun toString() = builder.toString()
}

/**
 * String that has already been sanitized
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
inline fun markdown(opt: MarkdownBuilder.() -> Unit = {}) =
    MarkdownBuilder().apply(opt)

/**
 * @param opt callback to configure [MarkdownBuilder]
 */
@BuilderTagMarker
inline fun buildMarkdown(opt: MarkdownBuilder.() -> Unit = {}) =
    MarkdownBuilder().apply(opt).toString()