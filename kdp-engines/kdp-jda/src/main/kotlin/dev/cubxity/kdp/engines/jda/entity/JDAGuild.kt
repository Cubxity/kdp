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

package dev.cubxity.kdp.engines.jda.entity

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.engines.jda.JDAEngine
import dev.cubxity.kdp.engines.jda.entity.channel.JDAGuildChannel
import dev.cubxity.kdp.engines.jda.util.await
import dev.cubxity.kdp.engines.jda.util.awaitOrNull
import dev.cubxity.kdp.entity.*
import dev.cubxity.kdp.exception.memberNotFound
import dev.cubxity.kdp.exception.roleNotFound
import dev.cubxity.kdp.exception.userNotFound
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse
import dev.cubxity.kdp.entity.Guild as KDPGuild

class JDAGuild(override val kdp: KDP<JDAEngine>, private val guild: Guild) : KDPGuild<JDAEngine> {
    override val id: Snowflake
        get() = guild.snowflake

    override val name: String
        get() = guild.name

    override val icon: Icon<JDAEngine>?
        get() = guild.iconId?.let { Icon(kdp, this, it) }

    override val splash: KDPGuild.Splash<JDAEngine>?
        get() = guild.splashId?.let { KDPGuild.Splash(kdp, this, it) }

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

    override val roles: Flow<JDARole>
        get() = guild.roleCache.asFlow().map { JDARole(kdp, it) }

    override val emojis: Flow<JDAEmoji>
        get() = guild.emoteCache.asFlow().map { JDAEmoji(kdp, it) }

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

    override val voiceStates: Flow<JDAVoiceState>
        get() = guild.voiceStates.asFlow().map { JDAVoiceState(kdp, it) }

    override val members: Flow<JDAMember>
        get() = guild.memberCache.asFlow().map { JDAMember(kdp, it) }

    override val channels: Flow<JDAGuildChannel>
        get() = guild.channels.asFlow().map { JDAGuildChannel(kdp, it) }

    override val maxPresences: Int
        get() = guild.maxPresences

    override val maxMembers: Int
        get() = guild.maxMembers

    override val vanityUrlCode: String?
        get() = guild.vanityCode

    override val description: String?
        get() = guild.description

    override val banner: KDPGuild.Banner<JDAEngine>?
        get() = guild.bannerId?.let { KDPGuild.Banner(kdp, this, it) }

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

    override suspend fun asGuild(): JDAGuild = this

    override suspend fun asGuildOrNull(): JDAGuild = this

    override suspend fun getMember(userId: Snowflake): JDAMember {
        try {
            return guild.retrieveMemberById(userId.value).await().let { JDAMember(kdp, it) }
        } catch (exception: ErrorResponseException) {
            throw when (exception.errorResponse) {
                ErrorResponse.UNKNOWN_MEMBER -> memberNotFound(id, userId)
                ErrorResponse.UNKNOWN_USER -> userNotFound(userId)
                else -> exception
            }
        }
    }

    override suspend fun getMemberOrNull(userId: Snowflake): JDAMember? =
        guild.retrieveMemberById(userId.value).awaitOrNull()?.let { JDAMember(kdp, it) }

    override suspend fun getRole(roleId: Snowflake): JDARole =
        getRoleOrNull(roleId) ?: roleNotFound(id, roleId)

    override suspend fun getRoleOrNull(roleId: Snowflake): JDARole? =
        guild.getRoleById(roleId.value)?.let { JDARole(kdp, it) }

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