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

package dev.cubxity.kdp.engine.jda.entity

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.behavior.GuildBehavior
import dev.cubxity.kdp.entity.Member
import dev.cubxity.kdp.entity.Role
import dev.cubxity.kdp.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime
import net.dv8tion.jda.api.entities.Member as JDAMember

@KDPUnsafe
inline class JDAMember(private val member: JDAMember) : Member {
    override val guild: GuildBehavior
        get() = JDAGuild(member.guild)

    override val user: User
        get() = JDAUser(member.user)

    override val nick: String?
        get() = member.nickname

    override val roles: Flow<Role>
        get() = member.roles.asFlow().map { JDARole(it) }

    override val joinedAt: OffsetDateTime
        get() = member.timeJoined

    override val premiumSince: String?
        get() = null // not supported

    override val isDeaf: Boolean
        get() = member.voiceState?.isDeafened == true

    override val isMute: Boolean
        get() = member.voiceState?.isMuted == true
}