/*
 *  KDP is a modular and customizable Discord command processing library.
 *  Copyright (C) 2020 Cubxity.
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

package dev.cubxity.kdp.entity

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.KDPObject
import dev.cubxity.kdp.engine.KDPEngine

interface Guild<TEngine : KDPEngine<TEngine>> : Entity<TEngine> {
    val name: String

    val icon: Icon<TEngine>

    data class Icon<TEngine : KDPEngine<TEngine>>(
        override val kdp: KDP<TEngine>,
        val guild: Guild<TEngine>,
        override val id: String?
    ) : KDPObject<TEngine>, ImageHolder {
        override fun get(format: ImageFormat): String? =
            id?.let { ICON_URL.format(guild.id, it, format.extension) }

        companion object {
            const val ICON_URL = "https://cdn.discordapp.com/icons/%s/%s.%s"
        }
    }
}