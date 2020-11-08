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

internal class PhaseContent<TSubject : Any, Call : Any>(val phase: PipelinePhase) {
    private val _interceptors: MutableList<PipelineInterceptor<TSubject, Call>> = ArrayList()

    val interceptors: List<PipelineInterceptor<TSubject, Call>>
        get() = _interceptors

    fun addInterceptor(interceptor: PipelineInterceptor<TSubject, Call>) {
        _interceptors += interceptor
    }
}