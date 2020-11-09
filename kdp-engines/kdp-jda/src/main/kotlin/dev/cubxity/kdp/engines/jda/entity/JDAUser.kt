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
import dev.cubxity.kdp.Transformer as KDPTransformer
import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.engines.jda.JDAEngine
import dev.cubxity.kdp.entity.PremiumType
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.entity.UserFlags
import dev.cubxity.kdp.entity.asSnowflake
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User
import dev.cubxity.kdp.entity.User as KDPUser

class JDAUser(override val kdp: KDP<JDAEngine>, private val user: User) : KDPUser<JDAEngine> {
    override val id: Snowflake
        get() = user.idLong.asSnowflake()

    @KDPUnsafe
    override val unsafe: User
        get() = user

    override val username: String
        get() = user.name

    override val discriminator: String
        get() = user.discriminator

    override val avatar: KDPUser.Avatar<JDAEngine>
        get() = KDPUser.Avatar(kdp, this, user.avatarId)

    override val isBot: Boolean?
        get() = user.isBot

    override val isMfaEnabled: Boolean?
        get() = (user as? SelfUser)?.isMfaEnabled

    override val locale: String?
        get() = null // Not supported

    override val flags: UserFlags?
        get() = UserFlags(user.flagsRaw)

    override val premiumType: PremiumType?
        get() = null // Not supported

    companion object Transformer : KDPTransformer<JDAEngine, User, JDAUser> {
        override fun transform(kdp: KDP<JDAEngine>, input: User): JDAUser =
            JDAUser(kdp, input)
    }
}