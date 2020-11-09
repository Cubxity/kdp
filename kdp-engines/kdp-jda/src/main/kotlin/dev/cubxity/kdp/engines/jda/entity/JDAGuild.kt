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
import dev.cubxity.kdp.entity.*
import net.dv8tion.jda.api.entities.Guild
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

    override val roles: List<Role<JDAEngine>>
        get() = TODO("Not yet implemented")

    override val emojis: List<Emoji<JDAEngine>>
        get() = TODO("Not yet implemented")

    override val features: List<GuildFeature>
        get() = TODO("Not yet implemented")

    override val mfaLevel: MFALevel
        get() = TODO("Not yet implemented")

    override val applicationId: Snowflake?
        get() = null // Not supported

    override val systemChannelId: Snowflake?
        get() = guild.systemChannel?.snowflake

    override val systemChannelFlags: SystemChannelFlags
        get() = TODO("Not yet implemented")

    override val rulesChannelId: Snowflake?
        get() = TODO("Not yet implemented")
    override val joinedAt: String
        get() = TODO("Not yet implemented")
    override val isLarge: Boolean?
        get() = TODO("Not yet implemented")
    override val isUnavailable: Boolean?
        get() = TODO("Not yet implemented")
    override val memberCount: Int?
        get() = TODO("Not yet implemented")
    override val voiceStates: List<VoiceState<JDAEngine>>?
        get() = TODO("Not yet implemented")
    override val members: List<Member<JDAEngine>>?
        get() = TODO("Not yet implemented")
    override val channels: List<Channel<JDAEngine>>?
        get() = TODO("Not yet implemented")
    override val maxPresences: Int?
        get() = TODO("Not yet implemented")
    override val maxMembers: Int?
        get() = TODO("Not yet implemented")
    override val vanityUrlCode: String?
        get() = TODO("Not yet implemented")
    override val description: String?
        get() = TODO("Not yet implemented")
    override val banner: dev.cubxity.kdp.entity.Guild.Banner<JDAEngine>?
        get() = TODO("Not yet implemented")
    override val premiumTier: Int
        get() = TODO("Not yet implemented")
    override val premiumSubscriptionCount: Int?
        get() = TODO("Not yet implemented")
    override val preferredLocale: String
        get() = TODO("Not yet implemented")
    override val publicUpdatesChannelId: Snowflake?
        get() = TODO("Not yet implemented")
    override val maxVideoChannelUsers: Int?
        get() = TODO("Not yet implemented")
    override val approximateMemberCount: Int?
        get() = TODO("Not yet implemented")
    override val approximatePresenceCount: Int?
        get() = TODO("Not yet implemented")

    private fun mapVerificationLevel(): VerificationLevel = when (guild.verificationLevel) {
        Guild.VerificationLevel.NONE -> VerificationLevel.None
        Guild.VerificationLevel.LOW -> VerificationLevel.Low
        Guild.VerificationLevel.MEDIUM -> VerificationLevel.Medium
        Guild.VerificationLevel.HIGH -> VerificationLevel.High
        Guild.VerificationLevel.VERY_HIGH -> VerificationLevel.VeryHigh
        Guild.VerificationLevel.UNKNOWN -> TODO()
    }

    private fun mapDefaultMessageNotifications(): DefaultMessageNotifications = when (guild.defaultNotificationLevel) {
        Guild.NotificationLevel.ALL_MESSAGES -> DefaultMessageNotifications.AllMessages
        Guild.NotificationLevel.MENTIONS_ONLY -> DefaultMessageNotifications.OnlyMentions
        Guild.NotificationLevel.UNKNOWN -> TODO()
    }

    private fun mapExplicitContentFilter(): ExplicitContentFilter = when (guild.explicitContentLevel) {
        Guild.ExplicitContentLevel.OFF -> ExplicitContentFilter.Disabled
        Guild.ExplicitContentLevel.NO_ROLE -> ExplicitContentFilter.MembersWithoutRoles
        Guild.ExplicitContentLevel.ALL -> ExplicitContentFilter.AllMembers
        Guild.ExplicitContentLevel.UNKNOWN -> TODO()
    }
}