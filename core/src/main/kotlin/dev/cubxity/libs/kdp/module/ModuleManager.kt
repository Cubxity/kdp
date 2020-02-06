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

package dev.cubxity.libs.kdp.module

import dev.cubxity.libs.kdp.KDP
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ModuleManager(private val kdp: KDP) {
    private val moduleInitializers = mutableListOf<ModuleInitializer<*>>()
    val modules = mutableListOf<Module>()

    fun <T : Module> register(vararg initializers: ModuleInitializer<T>) {
        initializers.forEach { moduleInitializers += it }
    }

    fun <T : Module> unregister(initializer: ModuleInitializer<T>) {
        moduleInitializers -= initializer
    }

    fun <T : Module> load(initializer: ModuleInitializer<T>) = initializer.initialize(kdp)

    suspend fun load() = coroutineScope {
        modules += moduleInitializers.map { async { load(it) } }.awaitAll()
    }

    suspend fun unload() = coroutineScope {
        modules.map { async { it.dispose() } }.awaitAll()
        modules.clear()
    }

    suspend fun reload() {
        load()
        unload()
    }
}