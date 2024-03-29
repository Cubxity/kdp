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
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.UserFlags
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User
import dev.cubxity.kdp.entity.User as KDPUser

@KDPUnsafe
inline class JDAUser(private val user: User) : KDPUser {
    override val id: Snowflake
        get() = user.snowflake

    override val username: String
        get() = user.name

    override val discriminator: String
        get() = user.discriminator

    override val avatar: KDPUser.Avatar
        get() = KDPUser.Avatar(this, user.avatarId)

    override val isBot: Boolean
        get() = user.isBot

    override val isMfaEnabled: Boolean?
        get() = (user as? SelfUser)?.isMfaEnabled

    override val flags: UserFlags
        get() = UserFlags(user.flagsRaw)
}