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

package dev.cubxity.kdp.engine

import dev.cubxity.kdp.KDP
import mu.KLogger
import kotlin.coroutines.CoroutineContext

class KDPEngineEnvironmentImpl(
    override val parentCoroutineContext: CoroutineContext,
    override val log: KLogger,
    override val token: String,
    private val modules: List<KDP.() -> Unit>
) : KDPEngineEnvironment {
    private var _kdp: KDP? = null

    override val kdp: KDP
        get() = _kdp ?: error("KDP has not been initialized")

    override fun start(engine: KDPEngine) {
        if (_kdp !== null) {
            error("KDP has already been initialized")
        }
        val kdp = KDP(parentCoroutineContext)
        kdp.start(engine)
        _kdp = kdp

        modules.forEach { kdp.it() }
    }

    override fun stop() {
        _kdp = null
        _kdp?.dispose()
    }
}