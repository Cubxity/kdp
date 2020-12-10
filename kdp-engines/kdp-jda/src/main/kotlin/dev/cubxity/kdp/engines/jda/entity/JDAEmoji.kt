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

package dev.cubxity.kdp.engines.jda.entity

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.engines.jda.JDAEngine
import dev.cubxity.kdp.entity.Emoji
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.ListedEmote

class JDAEmoji(override val kdp: KDP<JDAEngine>, private val emote: Emote) : Emoji<JDAEngine> {
    override val id: Snowflake
        get() = emote.snowflake

    override val name: String
        get() = emote.name

    override val roles: Flow<JDARole>
        get() = emote.roles.asFlow().map { JDARole(kdp, it) }

    override val user: User<JDAEngine>?
        get() = (emote as? ListedEmote)?.let { JDAUser(kdp, it.user) }

    override val isRequireColons: Boolean
        get() = true // not supported

    override val isManaged: Boolean
        get() = emote.isManaged

    override val isAnimated: Boolean
        get() = emote.isAnimated

    override val isAvailable: Boolean
        get() = emote.isAvailable
}