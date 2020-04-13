/*
 * KDP is a modular and customizable Discord command processing library.
 * Copyright (C) 2020 Cubxity.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline

fun AdminModule.debug() = AdminModule.debug {
    botAdmin = true
    handler {
        val command: String = args["command"]!!
        val ctx = createContext(executor, command)
        val startTime = System.currentTimeMillis()
        kdp.execute(
            ctx,
            CommandProcessingPipeline.POST_FILTER,
            CommandProcessingPipeline.MONITORING,
            CommandProcessingPipeline.PROCESS
        )
        val endTime = System.currentTimeMillis()
        send("Successfully ran $command, it took ${endTime - startTime}ms to run")
    }
}