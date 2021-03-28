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

package dev.cubxity.kdp.entity

import java.time.Instant

interface Activity {
    val name: String

    val activityType: ActivityType

    val url: String?

    val start: Instant?

    val stop: Instant?

    val applicationId: String?

    val details: String?

    val emoji: Emoji?

    val state: String?

    val party: Party?

    val assets: Assets?

    val secrets: Secrets?

    val flags: ActivityFlags

    data class Party(val id: String, val currentSize: Int, val maxSize: Int)
    data class Assets(val largeImage: String?, val largeText: String?, val smallImage: String?, val smallText: String?)
    data class Secrets(val join: String?, val spectate: String?, val match: String?)
}

enum class ActivityFlag(val code: Int) {
    Instance(0),
    Join(1),
    Spectate(2),
    JoinRequest(3),
    Sync(4),
    Play(5)
}

/**
 * A set of [activity flags][ActivityFlag].
 */
inline class ActivityFlags(val code: Int) {
    val flags: List<ActivityFlag>
        get() = ActivityFlag.values().filter { it in this }

    operator fun contains(flag: ActivityFlag): Boolean =
        code and flag.code != 0

    operator fun plus(flag: ActivityFlag): ActivityFlags =
        ActivityFlags(code or flag.code)

    operator fun plus(flags: ActivityFlags): ActivityFlags =
        ActivityFlags(code or flags.code)

    operator fun minus(flag: ActivityFlag): ActivityFlags =
        ActivityFlags(code xor flag.code)

    operator fun minus(flags: ActivityFlags): ActivityFlags =
        ActivityFlags(code xor flags.code)
}

sealed class ActivityType(val value: Int) {
    class Unknown(value: Int) : ActivityType(value)
    object Game : ActivityType(0)
    object Streaming : ActivityType(1)
    object Listening : ActivityType(2)
    object Watching : ActivityType(3)
    object Custom : ActivityType(4)
}