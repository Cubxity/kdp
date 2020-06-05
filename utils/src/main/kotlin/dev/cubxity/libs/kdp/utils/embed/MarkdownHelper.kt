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

@file:Suppress("NOTHING_TO_INLINE")

package dev.cubxity.libs.kdp.utils.embed

import dev.cubxity.libs.kdp.utils.BuilderTagMarker
import dev.cubxity.libs.kdp.utils.escapeMarkdown
import net.dv8tion.jda.api.utils.MarkdownUtil

/**
 * Wraps [text] in bold
 */
@BuilderTagMarker
inline fun bold(text: String) = text.bold


/**
 * Wraps text produced in [opt] in bold
 */
@BuilderTagMarker
inline fun bold(opt: MarkdownBuilder.() -> Unit) = buildMarkdown(opt).bold

@BuilderTagMarker
inline val String.bold
    get() = MarkdownUtil.bold(this).asMDString()

/**
 * Wraps [text] in italic
 */
@BuilderTagMarker
inline fun italic(text: String) =
    text.italic

@BuilderTagMarker
inline val String.italic
    get() = MarkdownUtil.italics(this).asMDString()

/**
 * Wraps text produced in [opt] in italic
 */
@BuilderTagMarker
inline fun italic(opt: MarkdownBuilder.() -> Unit) = buildMarkdown(opt).italic

/**
 * Wraps [text] in underline
 */
@BuilderTagMarker
inline fun underline(text: String) = text.underline

/**
 * Wraps text produced in [opt] in underline
 */
@BuilderTagMarker
inline fun underline(opt: MarkdownBuilder.() -> Unit) = buildMarkdown(opt).underline

@BuilderTagMarker
inline val String.underline
    get() = MarkdownUtil.underline(this).asMDString()

/**
 * Wraps [text] in underline
 */
@BuilderTagMarker
inline fun strikethrough(text: String) = text.strikethrough

/**
 * Wraps text produced in [opt] in underline
 * Note: text produced in [opt] might breaks the underline
 */
@BuilderTagMarker
inline fun strikethrough(opt: MarkdownBuilder.() -> Unit) = buildMarkdown(opt).strikethrough

@BuilderTagMarker
val String.strikethrough
    get() = MarkdownUtil.strike(this).asMDString()

/**
 * Wraps [text] in code
 */
@BuilderTagMarker
inline fun code(text: String) =
    text.code

/**
 * Wraps text produced in [opt] in code
 */
@BuilderTagMarker
inline fun code(opt: MarkdownBuilder.() -> Unit) = buildMarkdown(opt).code

@BuilderTagMarker
inline val String.code
    get() = MarkdownUtil.monospace(this).asMDString()

/**
 * Wraps [text] in codeBlock
 */
@BuilderTagMarker
inline fun codeBlock(text: String) = MarkdownUtil.codeblock(text).asMDString()

/**
 * Wraps [text] in codeBlock
 */
@BuilderTagMarker
inline fun codeBlock(text: String, language: String) = MarkdownUtil.codeblock(language, text).asMDString()

/**
 * Wraps text produced in [opt] in codeBlock
 */
@BuilderTagMarker
inline fun codeBlock(language: String = "", opt: MarkdownBuilder.() -> Unit) =
    MarkdownUtil.codeblock(language, buildMarkdown(opt)).asMDString()

/**
 * Wraps [text] in quote
 */
@BuilderTagMarker
inline fun quote(text: String) = text.quote

/**
 * Wraps text produced in [opt] in quote
 */
@BuilderTagMarker
inline fun quote(opt: MarkdownBuilder.() -> Unit) = buildMarkdown(opt).quote

@BuilderTagMarker
inline val String.quote
    get() = MarkdownUtil.quote(this).asMDString()

/**
 * Wraps [text] in codeBlock
 */
@BuilderTagMarker
inline fun spoiler(text: String) = text.spoiler

/**
 * Wraps text produced in [opt] in codeBlock
 */
@BuilderTagMarker
inline fun spoiler(opt: MarkdownBuilder.() -> Unit) = buildMarkdown(opt).spoiler

@BuilderTagMarker
inline val String.spoiler
    get() = MarkdownUtil.spoiler(this).asMDString()

/**
 * Creates a link
 */
@BuilderTagMarker
fun link(alt: String, url: String) = MarkdownUtil.maskedLink(alt, url).asMDString()