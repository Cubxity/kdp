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

package dev.cubxity.libs.kdp.utils

import dev.cubxity.libs.kdp.processing.CommandProcessingContext

private val USER_MENTION_REGEX = "<@(!?)(\\d{1,19})>".toRegex()
private val ROLE_MENTION_REGEX = "<@&(\\d{1,19})>".toRegex()

fun String.sanitize(ctx: CommandProcessingContext): String {
    val jda = ctx.event.jda
    var s = replace("@everyone", "@\u200Beveryone")
        .replace("@here", "@\u200Bhere")
        .replace(USER_MENTION_REGEX) {
            val id = it.groupValues[2]
            jda.getUserById(id)?.asTag?.let { t -> "@$t" }
                ?: "<@\u200B$id>"
        }
    ctx.guild?.also { g ->
        s = s.replace(ROLE_MENTION_REGEX) { r ->
            val id = r.groupValues[1]
            g.getRoleById(id)?.name?.let { n -> "@$n" }
                ?: "<@&\u200B$id>"
        }
    }
    return s
}

fun String.escapeMarkdown() = replace("*", "\\*")
    .replace("_", "\\_")
    .replace("`", "\\`")
    .replace("||", "\\||")
    .replace(">", "\\>")