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

package dev.cubxity.libs.kdp.pipeline

import org.slf4j.LoggerFactory

open class Pipeline<TContext : Any>(vararg phases: String) {
    private val logger = LoggerFactory.getLogger(javaClass)

    val features = mutableMapOf<String, Any>()
    val phases = mutableMapOf<String, MutableList<suspend PipelineContext<TContext>.() -> Unit>>()
        .apply { phases.forEach { put(it, mutableListOf()) } }

    /**
     * Intercepts the pipeline in the [phase], [opt] will be ran before the [phase] runs
     */
    fun intercept(phase: String, opt: suspend PipelineContext<TContext>.() -> Unit = {}) = phases[phase]?.add(opt)
        ?: throw IllegalArgumentException("Invalid phase")

    /**
     * Runs the pipeline with context [ctx]
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun execute(ctx: TContext) {
        val pipelineContext = PipelineContext(ctx)
        phases.forEach { (_, interceptors) ->
            (interceptors as (List<suspend PipelineContext<TContext>. () -> Unit>))
                .forEach { intercept ->
                    intercept(pipelineContext)
                    if (pipelineContext.isCancelled) return
                }
        }
    }

    /**
     * Runs the pipeline phase with context [ctx]
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun execute(ctx: TContext, phase: String) {
        val interceptors = phases[phase] ?: error("Invalid phase $phase")
        val pipelineContext = PipelineContext(ctx)
        (interceptors as (List<suspend PipelineContext<TContext>. () -> Unit>))
            .forEach { intercept ->
                try {
                    intercept(pipelineContext)
                } catch (t: Throwable) {
                    logger.error("Fatal error: pipeline failed on phase $phase", t)
                    return
                }
                if (pipelineContext.isCancelled) return
            }
    }
}