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

    val icon: Icon<TEngine>?

    val splash: Splash<TEngine>?

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

    val roles: List<Role<TEngine>>

    val emojis: List<Emoji<TEngine>>

    val features: List<GuildFeature>

    val mfaLevel: MFALevel

    val applicationId: Snowflake?

    val systemChannelId: Snowflake?

    val systemChannelFlags: SystemChannelFlags

    val rulesChannelId: Snowflake?

    val joinedAt: String

    val isLarge: Boolean?

    val isUnavailable: Boolean?

    val memberCount: Int?

    val voiceStates: List<VoiceState<TEngine>>?

    val members: List<Member<TEngine>>?

    val channels: List<Channel<TEngine>>?

    // presences

    val maxPresences: Int?

    val maxMembers: Int?

    val vanityUrlCode: String?

    val description: String?

    val banner: Banner<TEngine>?

    val premiumTier: Int

    val premiumSubscriptionCount: Int?

    val preferredLocale: String

    val publicUpdatesChannelId: Snowflake?

    val maxVideoChannelUsers: Int?

    val approximateMemberCount: Int?

    val approximatePresenceCount: Int?

    data class Splash<TEngine : KDPEngine<TEngine>>(
        override val kdp: KDP<TEngine>,
        val guild: Guild<TEngine>,
        override val id: String
    ) : KDPObject<TEngine>, ImageHolder {
        override fun get(format: ImageFormat): String =
            id.let { SPLASH_URL.format(guild.id, it, format.extension) }

        companion object {
            const val SPLASH_URL = "https://cdn.discordapp.com/splashes/%s/%s.%s"
        }
    }

    data class Banner<TEngine : KDPEngine<TEngine>>(
        override val kdp: KDP<TEngine>,
        val guild: Guild<TEngine>,
        override val id: String
    ) : KDPObject<TEngine>, ImageHolder {
        override fun get(format: ImageFormat): String =
            id.let { BANNER_URL.format(guild.id, it, format.extension) }

        companion object {
            const val BANNER_URL = "https://cdn.discordapp.com/banners/%s/%s.%s"
        }
    }
}

enum class VerificationLevel(val code: Int) {
    None(0),
    Low(1),
    Medium(2),
    High(3),
    VeryHigh(4)
}

enum class DefaultMessageNotifications(val code: Int) {
    AllMessages(0),
    OnlyMentions(1)
}

enum class ExplicitContentFilter(val code: Int) {
    Disabled(0),
    MembersWithoutRoles(1),
    AllMembers(2)
}

enum class GuildFeature(val value: String) {
    InviteSplash("INVITE_SPLASH"),
    VIPRegions("VIP_REGIONS"),
    VanityUrl("VANITY_URL"),
    Verified("VERIFIED"),
    Partnered("PARTNERED"),
    Public("PUBLIC"),
    Commerce("COMMERCE"),
    News("NEWS"),
    Discoverable("DISCOVERABLE"),
    Featureable("FEATURABLE"),
    AnimatedIcon("ANIMATED_ICON"),
    Banner("BANNER"),
    PublicDisabled("PUBLIC_DISABLED"),
    WelcomeScreenEnabled("WELCOME_SCREEN_ENABLED")
}

enum class MFALevel(val code: Int) {
    None(0),
    Elevated(1)
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