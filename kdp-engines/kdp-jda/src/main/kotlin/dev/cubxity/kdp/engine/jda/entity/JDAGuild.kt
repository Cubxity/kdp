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
import dev.cubxity.kdp.entity.*
import net.dv8tion.jda.api.entities.Guild
import dev.cubxity.kdp.entity.Guild as KDPGuild

@KDPUnsafe
inline class JDAGuild(private val guild: Guild) : KDPGuild {
    override val id: Snowflake
        get() = guild.snowflake

    override val name: String
        get() = guild.name

    override val icon: Icon?
        get() = guild.iconId?.let { Icon(this, it) }

    override val splash: KDPGuild.Splash?
        get() = guild.splashId?.let { KDPGuild.Splash(this, it) }

    override val isOwner: Boolean
        get() = guild.ownerIdLong == guild.selfMember.idLong

    override val ownerId: Snowflake
        get() = guild.ownerIdLong.asSnowflake()

    override val region: String
        get() = guild.regionRaw

    override val afkChannelId: Snowflake?
        get() = guild.afkChannel?.snowflake

    override val afkTimeout: Int
        get() = guild.afkTimeout.seconds

    override val isWidgetEnabled: Boolean?
        get() = null // Not supported

    override val widgetChannelId: Snowflake?
        get() = null // Not supported

    override val verificationLevel: VerificationLevel
        get() = mapVerificationLevel()

    override val defaultMessageNotifications: DefaultMessageNotifications
        get() = mapDefaultMessageNotifications()

    override val explicitContentFilter: ExplicitContentFilter
        get() = mapExplicitContentFilter()

    override val features: Set<GuildFeature>
        get() = mapFeatures()

    override val mfaLevel: MFALevel
        get() = mapMFALevel()

    override val applicationId: Snowflake?
        get() = null // Not supported

    override val systemChannelId: Snowflake?
        get() = guild.systemChannel?.snowflake

    override val systemChannelFlags: SystemChannelFlags
        get() = TODO("Not yet implemented")

    override val rulesChannelId: Snowflake?
        get() = null // Not supported

    override val isUnavailable: Boolean
        get() = guild.jda.isUnavailable(guild.idLong)

    override val memberCount: Int
        get() = guild.memberCount

    override val maxPresences: Int
        get() = guild.maxPresences

    override val maxMembers: Int
        get() = guild.maxMembers

    override val vanityUrlCode: String?
        get() = guild.vanityCode

    override val description: String?
        get() = guild.description

    override val banner: KDPGuild.Banner?
        get() = guild.bannerId?.let { KDPGuild.Banner(this, it) }

    override val premiumTier: Int
        get() = guild.boostTier.ordinal

    override val premiumSubscriptionCount: Int
        get() = guild.boostCount

    override val preferredLocale: String
        get() = guild.locale.toLanguageTag()

    override val publicUpdatesChannelId: Snowflake?
        get() = null // Not supported

    override val maxVideoChannelUsers: Int?
        get() = null // Not supported

    private fun mapVerificationLevel(): VerificationLevel = when (guild.verificationLevel) {
        Guild.VerificationLevel.NONE -> VerificationLevel.None
        Guild.VerificationLevel.LOW -> VerificationLevel.Low
        Guild.VerificationLevel.MEDIUM -> VerificationLevel.Medium
        Guild.VerificationLevel.HIGH -> VerificationLevel.High
        Guild.VerificationLevel.VERY_HIGH -> VerificationLevel.VeryHigh
        Guild.VerificationLevel.UNKNOWN -> VerificationLevel.Unknown(-1)
    }

    private fun mapDefaultMessageNotifications(): DefaultMessageNotifications = when (guild.defaultNotificationLevel) {
        Guild.NotificationLevel.ALL_MESSAGES -> DefaultMessageNotifications.AllMessages
        Guild.NotificationLevel.MENTIONS_ONLY -> DefaultMessageNotifications.OnlyMentions
        Guild.NotificationLevel.UNKNOWN -> DefaultMessageNotifications.Unknown(-1)
    }

    private fun mapExplicitContentFilter(): ExplicitContentFilter = when (guild.explicitContentLevel) {
        Guild.ExplicitContentLevel.OFF -> ExplicitContentFilter.Disabled
        Guild.ExplicitContentLevel.NO_ROLE -> ExplicitContentFilter.MembersWithoutRoles
        Guild.ExplicitContentLevel.ALL -> ExplicitContentFilter.AllMembers
        Guild.ExplicitContentLevel.UNKNOWN -> ExplicitContentFilter.Unknown(-1)
    }

    private fun mapFeatures(): Set<GuildFeature> = guild.features.mapNotNull {
        when (it) {
            "INVITE_SPLASH" -> GuildFeature.InviteSplash
            "VIP_REGIONS" -> GuildFeature.VIPRegions
            "VANITY_URL" -> GuildFeature.VanityUrl
            "VERIFIED" -> GuildFeature.Verified
            "PARTNERED" -> GuildFeature.Partnered
            "PUBLIC" -> GuildFeature.Public
            "COMMERCE" -> GuildFeature.Commerce
            "NEWS" -> GuildFeature.News
            "DISCOVERABLE" -> GuildFeature.Discoverable
            "FEATURABLE" -> GuildFeature.Featureable
            "ANIMATED_ICON" -> GuildFeature.AnimatedIcon
            "BANNER" -> GuildFeature.Banner
            "PUBLIC_DISABLED" -> GuildFeature.PublicDisabled
            "WELCOME_SCREEN_ENABLED" -> GuildFeature.WelcomeScreenEnabled
            else -> null
        }
    }.toSet()

    private fun mapMFALevel(): MFALevel = when (guild.requiredMFALevel) {
        Guild.MFALevel.NONE -> MFALevel.None
        Guild.MFALevel.TWO_FACTOR_AUTH -> MFALevel.Elevated
        Guild.MFALevel.UNKNOWN -> error("Unknown MFA level")
    }
}