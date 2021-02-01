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

package dev.cubxity.kdp.engines.jda

import dev.cubxity.kdp.engine.KDPEngineEnvironment
import dev.cubxity.kdp.engine.KDPEngineFactory

object JDA : KDPEngineFactory<JDAEngine, JDAEngine.Configuration> {
    override fun create(
        environment: KDPEngineEnvironment,
        configure: JDAEngine.Configuration.() -> Unit
    ): JDAEngine = JDAEngine(environment, configure)
}