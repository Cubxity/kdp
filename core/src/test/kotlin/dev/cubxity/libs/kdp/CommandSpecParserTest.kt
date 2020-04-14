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

package dev.cubxity.libs.kdp

import dev.cubxity.libs.kdp.command.CommandData
import dev.cubxity.libs.kdp.command.CommandSpecParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class CommandSpecParserTest {
    @Test
    fun parseEmptyTest() {
        assertThrows<IllegalStateException> { CommandSpecParser.parse("") }
    }

    @Test
    fun parseSingleTest() {
        assertDoesNotThrow {
            val data = CommandSpecParser.parse("test")
            assertEquals(listOf("test"), data.aliases)
            assertTrue(data.parameters.isEmpty())
        }
    }

    @Test
    fun parseMultiTest() {
        assertDoesNotThrow {
            val data = CommandSpecParser.parse("test|test2")
            assertEquals(listOf("test", "test2"), data.aliases)
            assertTrue(data.parameters.isEmpty())
        }
    }

    @Test
    fun parseSingleWithParameters() {
        assertDoesNotThrow {
            val data = CommandSpecParser.parse("test <p1> [p2...]")
            val params = listOf(
                CommandData.ParameterData("p1", required = true, vararg = false),
                CommandData.ParameterData("p2", required = false, vararg = true)
            )
            assertEquals(listOf("test"), data.aliases)
            assertEquals(params, data.parameters)
        }
    }
}