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
import dev.cubxity.libs.kdp.utils.escapeMarkdown

/**
 * Wraps [text] in bold
 */
@BuilderTagMarker
fun bold(text: String, escape: Boolean = true) = "**${text.escapeMarkdown()}**".asMDString()


/**
 * Wraps text produced in [opt] in bold
 * Note: text produced in [opt] might breaks the bold
 */
@BuilderTagMarker
fun bold(opt: MarkdownBuilder.() -> Unit) = "**${MarkdownBuilder().apply(opt)}**".asMDString()

@BuilderTagMarker
val String.bold
    get() = "**${escapeMarkdown()}**".asMDString()

/**
 * Wraps [text] in italic
 */
@BuilderTagMarker
fun italic(text: String) = "*${text.escapeMarkdown()}*".asMDString()

@BuilderTagMarker
val String.italic
    get() = "*${escapeMarkdown()}*".asMDString()

/**
 * Wraps text produced in [opt] in italic
 * Note: text produced in [opt] might breaks the italic
 */
@BuilderTagMarker
fun italic(opt: MarkdownBuilder.() -> Unit) = "*${MarkdownBuilder().apply(opt)}*".asMDString()

/**
 * Wraps [text] in underline
 */
@BuilderTagMarker
fun underline(text: String) = "__${text.escapeMarkdown()}__".asMDString()

/**
 * Wraps text produced in [opt] in underline
 * Note: text produced in [opt] might breaks the underline
 */
@BuilderTagMarker
fun underline(opt: MarkdownBuilder.() -> Unit) = "__${MarkdownBuilder().apply(opt)}__".asMDString()

@BuilderTagMarker
val String.underline
    get() = "__${escapeMarkdown()}__".asMDString()

/**
 * Wraps [text] in underline
 */
@BuilderTagMarker
fun strikethrough(text: String) = "~~${text.escapeMarkdown()}~~".asMDString()

/**
 * Wraps text produced in [opt] in underline
 * Note: text produced in [opt] might breaks the underline
 */
@BuilderTagMarker
fun strikethrough(opt: MarkdownBuilder.() -> Unit) = "~~${MarkdownBuilder().apply(opt)}~~".asMDString()

@BuilderTagMarker
val String.strikethrough
    get() = "~~${escapeMarkdown()}~~".asMDString()

/**
 * Wraps [text] in code
 */
@BuilderTagMarker
fun code(text: String) = "`${text.escapeMarkdown()}`".asMDString()

/**
 * Wraps text produced in [opt] in code
 * Note: text produced in [opt] might breaks the code
 */
@BuilderTagMarker
fun code(opt: MarkdownBuilder.() -> Unit) = "`${MarkdownBuilder().apply(opt)}`".asMDString()

@BuilderTagMarker
val String.code
    get() = "`${escapeMarkdown()}`".asMDString()

/**
 * Wraps [text] in codeBlock
 */
@BuilderTagMarker
fun codeBlock(text: String) = "```\n${text.escapeMarkdown()}\n```".asMDString()

/**
 * Wraps text produced in [opt] in codeBlock
 * Note: text produced in [opt] might breaks the codeBlock
 */
@BuilderTagMarker
fun codeBlock(language: String = "", opt: MarkdownBuilder.() -> Unit) =
    "```$language\n${MarkdownBuilder().apply(opt)}\n```".asMDString()

/**
 * Wraps [text] in codeBlock
 */
@BuilderTagMarker
fun spoiler(text: String) = "||${text.escapeMarkdown()}||".asMDString()

/**
 * Wraps text produced in [opt] in codeBlock
 * Note: text produced in [opt] might breaks the codeBlock
 */
@BuilderTagMarker
fun spoiler(opt: MarkdownBuilder.() -> Unit) =
    "||${MarkdownBuilder().apply(opt)}||".asMDString()

@BuilderTagMarker
val String.spoiler
    get() = "||${escapeMarkdown()}||".asMDString()

/**
 * Creates a link
 */
@BuilderTagMarker
fun link(alt: String, url: String) = "[${alt.escapeMarkdown()}]($url)".asMDString()