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

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.KDPObject
import dev.cubxity.kdp.behavior.GuildBehavior

interface Guild : GuildBehavior {
    val name: String

    val icon: Icon?

    val splash: Splash?

    val isOwner: Boolean

    val ownerId: Snowflake

    val region: String

    val afkChannelId: Snowflake?

    val afkTimeout: Int

    val isWidgetEnabled: Boolean?

    val widgetChannelId: Snowflake?

    val verificationLevel: VerificationLevel

    val defaultMessageNotifications: DefaultMessageNotifications

    val explicitContentFilter: ExplicitContentFilter

    val features: Set<GuildFeature>

    val mfaLevel: MFALevel

    val applicationId: Snowflake?

    val systemChannelId: Snowflake?

    val systemChannelFlags: SystemChannelFlags

    val rulesChannelId: Snowflake?

    val isUnavailable: Boolean?

    val memberCount: Int?

    // presences

    val maxPresences: Int?

    val maxMembers: Int?

    val vanityUrlCode: String?

    val description: String?

    val banner: Banner?

    val premiumTier: Int

    val premiumSubscriptionCount: Int?

    val preferredLocale: String

    val publicUpdatesChannelId: Snowflake?

    val maxVideoChannelUsers: Int?

    data class Splash(
        override val kdp: KDP,
        val guild: Guild,
        override val id: String
    ) : KDPObject, ImageHolder {
        override fun get(format: ImageFormat): String =
            id.let { SPLASH_URL.format(guild.id, it, format.extension) }

        companion object {
            const val SPLASH_URL = "https://cdn.discordapp.com/splashes/%s/%s.%s"
        }
    }

    data class Banner(
        override val kdp: KDP,
        val guild: Guild,
        override val id: String
    ) : KDPObject, ImageHolder {
        override fun get(format: ImageFormat): String =
            id.let { BANNER_URL.format(guild.id, it, format.extension) }

        companion object {
            const val BANNER_URL = "https://cdn.discordapp.com/banners/%s/%s.%s"
        }
    }
}

sealed class VerificationLevel(val value: Int) {
    class Unknown(value: Int) : VerificationLevel(value)
    object None : VerificationLevel(0)
    object Low : VerificationLevel(1)
    object Medium : VerificationLevel(2)
    object High : VerificationLevel(3)
    object VeryHigh : VerificationLevel(4)
}

sealed class DefaultMessageNotifications(val value: Int) {
    class Unknown(value: Int) : DefaultMessageNotifications(value)
    object AllMessages : DefaultMessageNotifications(0)
    object OnlyMentions : DefaultMessageNotifications(1)
}

sealed class ExplicitContentFilter(val value: Int) {
    class Unknown(value: Int) : ExplicitContentFilter(value)
    object Disabled : ExplicitContentFilter(0)
    object MembersWithoutRoles : ExplicitContentFilter(1)
    object AllMembers : ExplicitContentFilter(2)
}

sealed class GuildFeature(val value: String) {
    class Unknown(value: String) : GuildFeature(value)
    object InviteSplash : GuildFeature("INVITE_SPLASH")
    object VIPRegions : GuildFeature("VIP_REGIONS")
    object VanityUrl : GuildFeature("VANITY_URL")
    object Verified : GuildFeature("VERIFIED")
    object Partnered : GuildFeature("PARTNERED")
    object Public : GuildFeature("PUBLIC")
    object Commerce : GuildFeature("COMMERCE")
    object News : GuildFeature("NEWS")
    object Discoverable : GuildFeature("DISCOVERABLE")
    object Featureable : GuildFeature("FEATURABLE")
    object AnimatedIcon : GuildFeature("ANIMATED_ICON")
    object Banner : GuildFeature("BANNER")
    object PublicDisabled : GuildFeature("PUBLIC_DISABLED")
    object WelcomeScreenEnabled : GuildFeature("WELCOME_SCREEN_ENABLED")
}

sealed class MFALevel(val value: Int) {
    class Unknown(value: Int) : MFALevel(value)
    object None : MFALevel(0)
    object Elevated : MFALevel(1)
}

enum class SystemChannelFlag(val code: Int) {
    SuppressJoinNotifications(1 shl 0),
    SuppressPremiumSubscriptions(1 shl 1)
}

/**
 * A set of [system channel flags][SystemChannelFlags].
 */
inline class SystemChannelFlags(val code: Int) {
    val flags: List<SystemChannelFlag>
        get() = SystemChannelFlag.values().filter { it in this }

    operator fun contains(flag: SystemChannelFlag): Boolean =
        code and flag.code != 0

    operator fun plus(flag: SystemChannelFlag): SystemChannelFlags =
        SystemChannelFlags(code or flag.code)

    operator fun plus(flags: UserFlags): SystemChannelFlags =
        SystemChannelFlags(code or flags.code)

    operator fun minus(flag: SystemChannelFlag): SystemChannelFlags =
        SystemChannelFlags(code xor flag.code)

    operator fun minus(flags: UserFlags): SystemChannelFlags =
        SystemChannelFlags(code xor flags.code)
}