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

package dev.cubxity.libs.kdp.dsl

import dev.cubxity.libs.kdp.command.CommandData
import dev.cubxity.libs.kdp.command.CommandSpecParser
import dev.cubxity.libs.kdp.command.SubCommandData
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun command(spec: String, description: String?) = object : ReadOnlyProperty<Any, CommandData> {
    override fun getValue(thisRef: Any, property: KProperty<*>): CommandData {
        val (aliases, args) = CommandSpecParser.parse(spec)
        return object : CommandData {
            override val name
                get() = aliases.first()
            override val aliases: List<String>
                get() = aliases
            override val description
                get() = description
            override val args: List<CommandData.ParameterData>?
                get() = args
        }
    }
}

/**
 * Command delegate. It will return command data which contains the name and the description.
 * @param name the name of the command. If none provided, it will be using the property's name.
 * @param description the description of the command
 */
fun command(name: String? = null, description: String? = null, vararg aliases: String = emptyArray()) =
    object : ReadOnlyProperty<Any, CommandData> {
        override fun getValue(thisRef: Any, property: KProperty<*>) = object : CommandData {
            override val name
                get() = name ?: property.name
            override val aliases: List<String>
                get() = if (aliases.isEmpty()) listOf(this.name) else aliases.toList()
            override val description
                get() = description
        }
    }

fun CommandData.sub(spec: String, description: String? = null) =
    object : ReadOnlyProperty<Any, SubCommandData> {
        override fun getValue(thisRef: Any, property: KProperty<*>): SubCommandData {
            val (aliases, args) = CommandSpecParser.parse(spec)
            return object : SubCommandData {
                override val parent
                    get() = this@sub
                override val name
                    get() = aliases.first()
                override val aliases: List<String>
                    get() = if (aliases.isEmpty()) listOf(this.name) else aliases.toList()
                override val description
                    get() = description
                override val args: List<CommandData.ParameterData>?
                    get() = args
            }
        }
    }

/**
 * @param name the name of the sub command. If none provided, it will be using the property's name.
 * @param description the description of the command
 * Creates a sub command with a name, example:
 * !status discord
 */
fun CommandData.sub(name: String? = null, description: String? = null, vararg aliases: String = emptyArray()) =
    object : ReadOnlyProperty<Any, SubCommandData> {
        override fun getValue(thisRef: Any, property: KProperty<*>) = object : SubCommandData {
            override val parent
                get() = this@sub
            override val name
                get() = name ?: property.name
            override val aliases: List<String>
                get() = if (aliases.isEmpty()) listOf(this.name) else aliases.toList()
            override val description
                get() = description
        }
    }