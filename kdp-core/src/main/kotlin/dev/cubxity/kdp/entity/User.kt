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

interface User<TEngine : KDPEngine<TEngine>> : Entity<TEngine> {
    val username: String

    val discriminator: String

    val avatar: Avatar<TEngine>

    val isBot: Boolean?

    val isMfaEnabled: Boolean?

    val flags: UserFlags?

    data class Avatar<TEngine : KDPEngine<TEngine>>(
        override val kdp: KDP<TEngine>,
        val user: User<TEngine>,
        override val id: String?
    ) : KDPObject<TEngine>, ImageHolder {
        inline val isCustom: Boolean
            get() = id !== null

        inline val defaultUrl: String
            get() = DEFAULT_AVATAR_URL.format(user.id)

        override fun get(format: ImageFormat): String =
            id?.let { AVATAR_URL.format(user.id.value, it, format.extension) } ?: defaultUrl

        companion object {
            const val AVATAR_URL = "https://cdn.discordapp.com/avatars/%d/%s.%s"
            const val DEFAULT_AVATAR_URL = "https://cdn.discordapp.com/embed/avatars/%d.png"
        }
    }
}

/**
 * See: https://discord.com/developers/docs/resources/user#user-object-user-flags
 */
enum class UserFlag(val code: Int) {
    DiscordEmployee(1 shl 0),
    DiscordPartner(1 shl 1),
    HypeSquad(1 shl 2),
    BugHunterLevel1(1 shl 3),
    HouseBravery(1 shl 6),
    HouseBrilliance(1 shl 7),
    HouseBalance(1 shl 8),
    EarlySupporter(1 shl 9),
    TeamUser(1 shl 10),
    System(1 shl 12),
    BugHunterLevel2(1 shl 14),
    VerifiedBot(1 shl 16),
    VerifiedBotDeveloper(1 shl 17)
}

/**
 * A set of [user flags][UserFlag].
 */
inline class UserFlags(val code: Int) {
    val flags: List<UserFlag>
        get() = UserFlag.values().filter { it in this }

    operator fun contains(flag: UserFlag): Boolean =
        code and flag.code != 0

    operator fun plus(flag: UserFlag): UserFlags =
        UserFlags(code or flag.code)

    operator fun plus(flags: UserFlags): UserFlags =
        UserFlags(code or flags.code)

    operator fun minus(flag: UserFlag): UserFlags =
        UserFlags(code xor flag.code)

    operator fun minus(flags: UserFlags): UserFlags =
        UserFlags(code xor flags.code)
}

enum class PremiumType(val code: Int) {
    None(0),
    NitroClassic(1),
    Nitro(2)
}