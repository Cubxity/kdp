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
import dev.cubxity.kdp.engine.jda.entity.channel.JDAGuildChannel
import dev.cubxity.kdp.entity.VoiceState
import net.dv8tion.jda.api.entities.GuildVoiceState

@KDPUnsafe
inline class JDAVoiceState(private val state: GuildVoiceState) : VoiceState {
    override val guild: JDAGuild
        get() = JDAGuild(state.guild)

    override val channel: JDAGuildChannel?
        get() = null // TODO

    override val member: JDAMember
        get() = JDAMember(state.member)

    override val sessionId: String?
        get() = state.sessionId

    override val isDeaf: Boolean
        get() = state.isGuildDeafened

    override val isMute: Boolean
        get() = state.isGuildMuted

    override val isSelfDeaf: Boolean
        get() = state.isSelfDeafened

    override val isSelfMute: Boolean
        get() = state.isSelfMuted

    override val isSelfStream: Boolean
        get() = state.isStream

    override val isSelfVideo: Boolean
        get() = false // Not supported

    override val isSuppress: Boolean
        get() = state.isSuppressed
}