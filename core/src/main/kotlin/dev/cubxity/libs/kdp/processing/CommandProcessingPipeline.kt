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

package dev.cubxity.libs.kdp.processing

import dev.cubxity.libs.kdp.pipeline.Pipeline

open class CommandProcessingPipeline : Pipeline<CommandProcessingContext>(
    FILTER,
    MATCH,
    MONITORING,
    PROCESS,
    ERROR
) {
    companion object {
        /**
         * Phase for filtering messages
         */
        const val FILTER = "Filter"

        /**
         * Phase for processing arguments and matching command
         */
        const val MATCH = "Match"

        /**
         * Phase for tracing commands, useful for logging, metrics, etc..
         */
        const val MONITORING = "Monitoring"

        /**
         * Phase for processing command and sending a response
         */
        const val PROCESS = "Process"

        /**
         * Phase for handling exceptions under processing
         */
        const val ERROR = "Error"
    }
}