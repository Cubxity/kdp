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

package dev.cubxity.libs.kdp.serialization.serializers

import dev.cubxity.libs.kdp.processing.CommandProcessingContext
import dev.cubxity.libs.kdp.serialization.ArgumentSerializer

class IntSerializer : ArgumentSerializer<Int> {
    override suspend fun serialize(ctx: CommandProcessingContext, s: String) = s.toIntOrNull()
}

class DoubleSerializer : ArgumentSerializer<Double> {
    override suspend fun serialize(ctx: CommandProcessingContext, s: String) = s.toDoubleOrNull()?.takeIf { it.isFinite() }
}

class LongSerializer : ArgumentSerializer<Long> {
    override suspend fun serialize(ctx: CommandProcessingContext, s: String) = s.toLongOrNull()
}

class FloatSerializer : ArgumentSerializer<Float> {
    override suspend fun serialize(ctx: CommandProcessingContext, s: String) = s.toFloatOrNull()?.takeIf { it.isFinite() }
}
