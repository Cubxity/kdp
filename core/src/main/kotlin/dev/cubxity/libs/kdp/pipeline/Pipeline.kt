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

open class Pipeline<TContext>(vararg phases: String) {
    val features = mutableMapOf<String, Any>()
    val phases = hashMapOf<String, MutableList<TContext.() -> Unit>>().apply { phases.forEach { put(it, mutableListOf()) } }

    /**
     * Intercepts the pipeline in the [phase], [opt] will be ran before the [phase] runs
     */
    fun intercept(phase: String, opt: TContext.() -> Unit = {}) = phases[phase]?.add(opt)
            ?: throw IllegalArgumentException("Invalid phase")

    /**
     * Runs the pipeline phase with context [ctx]
     */
    fun run(phase: String, ctx: TContext, opt: TContext.() -> Unit = {}) {
        if (phases.containsKey(phase)) {
            phases[phase]!!.forEach { it(ctx) }
            opt(ctx)
        } else throw IllegalArgumentException("Invalid phase")
    }
}