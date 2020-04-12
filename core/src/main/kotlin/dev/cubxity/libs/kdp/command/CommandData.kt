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

package dev.cubxity.libs.kdp.command

interface CommandData {
    val name: String

    val spec: String
        get() = name
    val path: String
        get() = name

    val aliases: List<String>
        get() = listOf(name)

    val description: String?

    val args: List<ParameterData>?
        get() = null

    fun build() = Command(name, description, aliases, args, path, spec)

    data class ParameterData(val name: String, val required: Boolean, val vararg: Boolean)
}