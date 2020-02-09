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

package dev.cubxity.libs.kdp.processing

class ArgumentsContainer(val ctx: CommandProcessingContext) {
    val cache: MutableMap<Int, Any?> = mutableMapOf()

    inline operator fun <reified T> get(name: String): T? {
        val spec = ctx.command?.args ?: error("Command does not have an argument spec. Use get(int) instead.")
        val arg = spec.find { it.name == name } ?: error("Argument with name $name not found.")
        val i = spec.indexOf(arg)
        cache[i]?.also { return it as T? }

        val rawArgs = ctx.rawArgs ?: error("Context's arguments has not been parsed.")
        if (rawArgs.size <= i) return null

        val s = if (arg.vararg) rawArgs.subList(i, rawArgs.size).joinToString(" ")
        else rawArgs[i]

        val v: T? = ctx.kdp.serializationFactory.serialize(ctx, s)
        cache[i] = v
        return v
    }

    inline operator fun <reified T> get(i: Int): T? {
        cache[i]?.also { return it as T? }

        val rawArgs = ctx.rawArgs ?: error("Context's arguments has not been parsed.")
        val s = rawArgs.getOrNull(i) ?: return null

        val v: T? = ctx.kdp.serializationFactory.serialize(ctx, s)
        cache[i] = v
        return v
    }
}