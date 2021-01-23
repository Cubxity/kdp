/*
 *  KDP is a modular and customizable Discord command processing library.
 *  Copyright (C) 2020-2021 Cubxity.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.kdp

import dev.cubxity.kdp.engine.KDPEngine
import dev.cubxity.kdp.engine.KDPEngineEnvironment
import dev.cubxity.kdp.engine.KDPEngineEnvironmentImpl
import dev.cubxity.kdp.engine.KDPEngineFactory
import dev.cubxity.kdp.processing.ProcessingPipeline
import kotlinx.coroutines.*
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

class KDP<TEngine : KDPEngine<TEngine>>(
    parentCoroutineContext: CoroutineContext = Dispatchers.Default
) : ProcessingPipeline(), CoroutineScope {
    private val job = SupervisorJob(parentCoroutineContext[Job])
    private var _engine: TEngine? = null

    override val coroutineContext: CoroutineContext = parentCoroutineContext + job

    val engine: TEngine
        get() = _engine ?: error("KDP has not been started")

    fun start(engine: TEngine) {
        if (!isActive) error("KDP has already been disposed")

        if (_engine !== null) {
            if (_engine !== engine) {
                error("KDP has already been started with another engine")
            } else {
                return
            }
        }

        _engine = engine
    }

    fun dispose() {
        job.cancel()
        _engine = null
    }
}

/**
 * Initializes [KDP] with the given [factory].
 *
 * @param token the token used for logging into Discord.
 * @param configure configuration block for the engine.
 * @param module KDP module block.
 */
fun <TEngine : KDPEngine<TEngine>, TConfiguration : KDPEngine.Configuration> kdp(
    factory: KDPEngineFactory<TEngine, TConfiguration>,
    token: String,
    parentCoroutineContext: CoroutineContext = Dispatchers.Default,
    configure: TConfiguration.() -> Unit = {},
    module: KDP<TEngine>.() -> Unit = {}
): TEngine {
    val environment = KDPEngineEnvironmentImpl(
        parentCoroutineContext,
        KotlinLogging.logger("KDP"),
        token,
        listOf(module)
    )

    return kdp(factory, environment, configure)
}

/**
 * Creates [KDP] with the given [factory], [environment] and [configure] block.
 */
fun <TEngine : KDPEngine<TEngine>, TConfiguration : KDPEngine.Configuration> kdp(
    factory: KDPEngineFactory<TEngine, TConfiguration>,
    environment: KDPEngineEnvironment<TEngine>,
    configure: TConfiguration.() -> Unit = {}
): TEngine {
    return factory.create(environment, configure)
}