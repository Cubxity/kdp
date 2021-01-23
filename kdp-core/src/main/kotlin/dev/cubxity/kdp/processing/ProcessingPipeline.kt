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

package dev.cubxity.kdp.processing

import dev.cubxity.kdp.pipeline.Pipeline
import dev.cubxity.kdp.pipeline.PipelinePhase

open class ProcessingPipeline : Pipeline<Unit, ProcessingContext>(Setup, Monitoring, Features, Call, Fallback) {
    companion object ProcessingPhase {
        /**
         * Phase for preparing execution and it's attributes for processing.
         */
        val Setup: PipelinePhase = PipelinePhase("Setup")

        /**
         * Phase for tracing executions, useful for logging, metrics, error handling and so on.
         */
        val Monitoring: PipelinePhase = PipelinePhase("Monitoring")

        /**
         * Phase for features. Most features should intercept this phase.
         */
        val Features: PipelinePhase = PipelinePhase("Features")

        /**
         * Phase for processing an execution and reacting to it.
         */
        val Call: PipelinePhase = PipelinePhase("Call")

        /**
         * Phase for handling unprocessed executions.
         */
        val Fallback: PipelinePhase = PipelinePhase("Fallback")
    }
}