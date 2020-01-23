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

package dev.cubxity.libs.kdp

import dev.cubxity.libs.kdp.command.CommandData
import dev.cubxity.libs.kdp.command.CommandStringParser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class CommandStringParserTest {
    @Test
    fun parseEmptyTest() {
        assert(true == false)
        assertThrows<IllegalStateException> { CommandStringParser.parse("") }
    }

    @Test
    fun parseSingleTest() {
        assertDoesNotThrow {
            val data = CommandStringParser.parse("test")
            assert(data.aliases == listOf("test"))
            assert(data.parameters.isEmpty())
        }
    }

    @Test
    fun parseMultiTest() {
        assertDoesNotThrow {
            val data = CommandStringParser.parse("test|test2")
            assert(data.aliases == listOf("test", "test2"))
            assert(data.parameters.isEmpty())
        }
    }

    @Test
    fun parseSingleWithParameters() {
        assertDoesNotThrow {
            val data = CommandStringParser.parse("test <p1> [p2...]")
            val params = listOf(
                CommandData.ParameterData("p1", required = true, vararg = false),
                CommandData.ParameterData("p2", required = false, vararg = true)
            )
            assert(data.aliases == listOf("test"))
            assert(data.parameters == params)
        }
    }
}
