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

package dev.cubxity.kdp.engine.jda.event.message.guild.react

import dev.cubxity.kdp.annotation.KDPUnsafe
import dev.cubxity.kdp.behavior.GuildBehavior
import dev.cubxity.kdp.behavior.MemberBehavior
import dev.cubxity.kdp.behavior.UserBehavior
import dev.cubxity.kdp.engine.jda.entity.JDAGuild
import dev.cubxity.kdp.engine.jda.entity.JDAMember
import dev.cubxity.kdp.engine.jda.event.message.guild.JDAGuildMessageEvent
import dev.cubxity.kdp.entity.ReactionEmoji
import dev.cubxity.kdp.entity.asSnowflake
import dev.cubxity.kdp.event.message.guild.react.GuildMessageReactionEvent
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent

@KDPUnsafe
interface JDAGuildMessageReactionEvent : JDAGuildMessageEvent, GuildMessageReactionEvent {
    override val event: GenericGuildMessageReactionEvent

    override val user: UserBehavior
        get() = UserBehavior(event.userIdLong.asSnowflake())

    override val member: MemberBehavior
        get() = event.member?.let { JDAMember(it) } ?: MemberBehaviorImpl(event)

    override val emoji: ReactionEmoji
        get() = mapReaction()

    private inline class MemberBehaviorImpl(private val event: GenericGuildMessageReactionEvent) : MemberBehavior {
        override val guild: GuildBehavior
            get() = JDAGuild(event.guild)

        override val user: UserBehavior
            get() = UserBehavior(event.userIdLong.asSnowflake())
    }

    private fun mapReaction(): ReactionEmoji = event.reactionEmote.run {
        when {
            isEmoji -> ReactionEmoji.Unicode(name)
            else -> ReactionEmoji.Custom(idLong.asSnowflake(), name, emote.isAnimated)
        }
    }
}