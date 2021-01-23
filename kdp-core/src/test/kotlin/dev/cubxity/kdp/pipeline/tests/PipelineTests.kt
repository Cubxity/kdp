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

package dev.cubxity.kdp.pipeline.tests

import dev.cubxity.kdp.pipeline.PipelinePhase
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class PipelineTests {
    @Test
    fun testExecution() = runBlocking {
        val pipeline = TestPipeline()

        pipeline.intercept(TestPipeline.Three) {
            assertEquals(2, subject)
            proceedWith(3)
        }
        pipeline.intercept(TestPipeline.Two) {
            assertEquals(1, subject)
            proceedWith(2)
        }
        pipeline.intercept(TestPipeline.One) {
            proceedWith(1)
        }

        val result = pipeline.execute(Unit, 0)
        assertEquals(3, result)
    }

    @Test
    fun testFinishExecution() = runBlocking {
        val pipeline = TestPipeline()

        pipeline.intercept(TestPipeline.Two) {
            proceedWith(2)
        }
        pipeline.intercept(TestPipeline.One) {
            finish()
        }

        val result = pipeline.execute(Unit, 0)
        assertEquals(result, 0)
    }

    @Test
    fun testExecutionException(): Unit = runBlocking {
        val pipeline = TestPipeline()

        pipeline.intercept(TestPipeline.One) {
            throw Throwable()
        }

        assertFails {
            pipeline.execute(Unit, 0)
        }
    }

    @Test
    fun testInterceptUnregisteredPhase(): Unit = runBlocking {
        val pipeline = TestPipeline()

        assertFailsWith<IllegalStateException> {
            pipeline.intercept(PipelinePhase("")) {}
        }
    }
}