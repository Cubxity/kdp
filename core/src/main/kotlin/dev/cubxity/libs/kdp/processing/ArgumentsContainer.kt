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

package dev.cubxity.libs.kdp.processing

import kotlinx.coroutines.runBlocking

class ArgumentsContainer(val ctx: CommandProcessingContext) {
    @Deprecated("This method is blocking. Use receive instead.", ReplaceWith("receive(name)"))
    inline operator fun <reified T> get(name: String): T? =
        runBlocking { receiveOrNull(name) }

    @Deprecated("This method is blocking. Use receive instead.", ReplaceWith("receive(i)"))
    inline operator fun <reified T> get(i: Int): T? =
        runBlocking { receiveOrNull(i) }

    suspend inline fun <reified T> receive(name: String): T =
        receiveOrNull(name) ?: error("Unable to retrieve argument '$name'")

    suspend inline fun <reified T> receiveOrNull(name: String): T? {
        val spec = ctx.command?.args ?: error("Command does not have an argument spec. Use receive(int) instead.")
        val arg = spec.find { it.name == name } ?: error("Argument with name $name not found.")
        val i = spec.indexOf(arg)

        val rawArgs = ctx.rawArgs ?: error("Context's arguments has not been parsed.")
        if (rawArgs.size <= i) return null

        val s = if (arg.vararg) rawArgs.subList(i, rawArgs.size).joinToString(" ")
        else rawArgs[i]

        return ctx.kdp.serializationFactory.serialize(ctx, s)
    }

    suspend inline fun <reified T> receive(i: Int): T =
        receiveOrNull(i) ?: error("Unable to retrieve argument at index $i")

    suspend inline fun <reified T> receiveOrNull(i: Int): T? {
        val rawArgs = ctx.rawArgs ?: error("Context's arguments has not been parsed.")
        val s = rawArgs.getOrNull(i) ?: return null

        return ctx.kdp.serializationFactory.serialize(ctx, s)
    }

}