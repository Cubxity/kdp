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
    private val _modules = mutableListOf<Module>()
    val modules: List<Module>
        get() = _modules

    fun register(vararg modules: Module) {
        modules.forEach { _modules += it }
    }

    fun unregister(module: Module) {
        _modules -= module
    }

    suspend fun load() = coroutineScope {
        modules.map { async { it.init() } }.awaitAll()
    }

    suspend fun unload() = coroutineScope {
        _modules.map { async { it.dispose() } }.awaitAll()
    }

    suspend fun reload() {
        unload()
        load()
    }
}
