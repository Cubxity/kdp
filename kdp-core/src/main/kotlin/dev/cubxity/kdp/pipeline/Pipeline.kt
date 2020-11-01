/*
 *  KDP is a modular and customizable Discord command processing library.
 *  Copyright (C) 2020 Cubxity.
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

package dev.cubxity.kdp.pipeline

/**
 * Represents an execution pipeline.
 */
open class Pipeline<TSubject : Any, TContext : Any>(vararg phases: PipelinePhase) {
    private val _phases: List<PhaseContent<*, *>> =
        phases.map { PhaseContent<Any, Any>(it) }

    private val interceptors: MutableList<PipelineInterceptor<TSubject, TContext>> = ArrayList()

    fun intercept(phase: PipelinePhase, interceptor: PipelineInterceptor<TSubject, TContext>) {
        val phaseContent = findPhase(phase)
            ?: error("Phase $phase was not registered for this pipeline")

        var index = 0
        for (p in _phases) {
            index += p.interceptors.size
            if (p === phaseContent) break
        }

        interceptors.add(index, interceptor)
        phaseContent.addInterceptor(interceptor)
    }

    suspend fun execute(context: TContext, subject: TSubject): TSubject =
        createContext(context, subject).execute(subject)

    private fun createContext(context: TContext, subject: TSubject): PipelineExecutor<TSubject> =
        SuspendFunctionGun(subject, context, interceptors)

    @Suppress("UNCHECKED_CAST")
    private fun findPhase(phase: PipelinePhase): PhaseContent<TSubject, TContext>? =
        _phases.find { it.phase === phase } as? PhaseContent<TSubject, TContext>
}

suspend inline fun <TContext : Any> Pipeline<Unit, TContext>.execute(context: TContext): Unit =
    execute(context, Unit)

typealias PipelineInterceptor<TSubject, TContext> = suspend PipelineContext<TSubject, TContext>.(TSubject) -> Unit