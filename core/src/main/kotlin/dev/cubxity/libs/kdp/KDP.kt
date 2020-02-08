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

package dev.cubxity.libs.kdp

import club.minnced.jda.reactor.ReactiveEventManager
import dev.cubxity.libs.kdp.module.Module
import dev.cubxity.libs.kdp.module.ModuleInitializer
import dev.cubxity.libs.kdp.module.ModuleManager
import dev.cubxity.libs.kdp.module.ReflectionModuleInitializer
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline
import dev.cubxity.libs.kdp.respond.RespondPipeline
import dev.cubxity.libs.kdp.serialization.SerializationFactory
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

class KDP : CommandProcessingPipeline() {
    val manager = ReactiveEventManager()
    val moduleManager = ModuleManager(this)
    val respondPipeline = RespondPipeline()
    var serializationFactory = SerializationFactory()

    fun <T : Module> ModuleInitializer<T>.register() {
        moduleManager.register(this)
    }

    fun <T : Module> Class<T>.register() {
        moduleManager.register(ReflectionModuleInitializer(this))
    }

    fun <T : Module> KClass<T>.register() {
        moduleManager.register(ReflectionModuleInitializer(this.java))
    }

    fun init() {
        runBlocking {
            moduleManager.load()
        }
    }
}

fun kdp(opt: KDP.() -> Unit) = KDP().apply(opt)