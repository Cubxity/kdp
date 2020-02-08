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
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.*
import java.awt.Color

/**
 * @author Cubxity
 * @since 6/10/2019
 */
class WrappedEmbedBuilder : EmbedBuilder() {
    /**
     * Extension to set title
     */
    var title: String?
        get() = null
        set(value) {
            setTitle(value)
        }

    /**
     * Extension to set color
     */
    var color: Color?
        get() = null
        set(value) {
            setColor(value)
        }

    /**
     * Extension to set footer
     */
    var footer: String?
        get() = null
        set(value) {
            setFooter(value)
        }

    /**
     * Extension to set image
     */
    var image: String?
        get() = null
        set(value) {
            setImage(value)
        }

    /**
     * Appends a string and escape it
     */
    operator fun String.unaryPlus() = appendDescription(escapeMarkdown())

    /**
     * Append a [MDString]
     */
    operator fun MDString.unaryPlus() = appendDescription(toString())

    operator fun List<MDString>.unaryPlus() {
        var first = true
        forEach {
            if (first)
                first = false
            else
                appendDescription(", ")
            +it
        }
    }

    operator fun User.unaryPlus() {
        appendDescription(asMention)
    }

    operator fun Member.unaryPlus() {
        appendDescription(asMention)
    }

    operator fun Role.unaryPlus() {
        appendDescription(asMention)
    }

    operator fun Emote.unaryPlus() {
        appendDescription(asMention)
    }

    operator fun TextChannel.unaryPlus() {
        appendDescription(asMention)
    }

    /**
     * Creates a field then configure it with [opt]
     * @param name field's name
     * @param inline
     */
    @BuilderTagMarker
    fun field(name: String, inline: Boolean = false, opt: MarkdownBuilder.() -> Unit) {
        addField(name, MarkdownBuilder().apply(opt).toString(), inline)
    }
}

/**
 * Creates an embed using [WrappedEmbedBuilder]
 * @param opt callback to configure [WrappedEmbedBuilder]
 */
@BuilderTagMarker
fun embed(opt: WrappedEmbedBuilder.() -> Unit = {}) =
    WrappedEmbedBuilder().apply(opt).build()