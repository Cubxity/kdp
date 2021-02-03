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

package dev.cubxity.kdp.engines.jda.behavior

import dev.cubxity.kdp.KDP
import dev.cubxity.kdp.behavior.MemberBehavior
import dev.cubxity.kdp.engines.jda.entity.JDAGuild
import dev.cubxity.kdp.engines.jda.entity.JDAMember
import dev.cubxity.kdp.engines.jda.entity.snowflake
import dev.cubxity.kdp.engines.jda.util.await
import dev.cubxity.kdp.engines.jda.util.awaitOrNull
import dev.cubxity.kdp.entity.Member
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.exception.guildNotFound
import dev.cubxity.kdp.exception.memberNotFound
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse

class JDAMemberBehavior(
    kdp: KDP,
    id: Snowflake,
    private val jdaGuild: Guild
) : JDAUserBehavior(kdp, id, jdaGuild.jda), MemberBehavior {
    override val guildId: Snowflake
        get() = jdaGuild.snowflake

    override val guild: JDAGuild
        get() = JDAGuild(kdp, jdaGuild)

    override suspend fun asMember(): Member {
        try {
            return JDAMember(kdp, jdaGuild.retrieveMemberById(id.value).await())
        } catch (exception: ErrorResponseException) {
            throw when (exception.errorResponse) {
                ErrorResponse.UNKNOWN_GUILD -> guildNotFound(id)
                ErrorResponse.UNKNOWN_MEMBER -> memberNotFound(guildId, id)
                else -> exception
            }
        }
    }

    override suspend fun asMemberOrNull(): Member? =
        jdaGuild.retrieveMemberById(id.value).awaitOrNull()?.let { JDAMember(kdp, it) }
}