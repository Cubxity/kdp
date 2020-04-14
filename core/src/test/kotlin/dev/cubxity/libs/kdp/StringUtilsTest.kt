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

import dev.cubxity.libs.kdp.dsl.command
import dev.cubxity.libs.kdp.dsl.sub
import dev.cubxity.libs.kdp.utils.argsSpec
import dev.cubxity.libs.kdp.utils.path
import dev.cubxity.libs.kdp.utils.displaySpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringUtilsTest {
    private val simple by command()
    private val parent by command("parent <arg1> [arg2] [args3...]", null)
    private val child by parent.sub("child <arg1> [arg2] [args3...]")
    private val deepChild by child.sub("deepChild <arg1> [arg2] [args3...]")

    @Test
    fun pathTest() {
        assertEquals("parent", parent.path)
        assertEquals("parent child", child.path)
        assertEquals("parent child deepChild", deepChild.path)

    }

    @Test
    fun argsSpecTest() {
        assertEquals("", simple.argsSpec)
        assertEquals("<arg1> [arg2] [args3...]", parent.argsSpec)
        assertEquals("<arg1> [arg2] [args3...]", child.argsSpec)
        assertEquals("<arg1> [arg2] [args3...]", deepChild.argsSpec)
    }

    @Test
    fun displaySpecTest() {
        assertEquals("simple", simple.displaySpec)
        assertEquals("parent <arg1> [arg2] [args3...]", parent.displaySpec)
        assertEquals("parent child <arg1> [arg2] [args3...]", child.displaySpec)
        assertEquals("parent child deepChild <arg1> [arg2] [args3...]", deepChild.displaySpec)
    }
}