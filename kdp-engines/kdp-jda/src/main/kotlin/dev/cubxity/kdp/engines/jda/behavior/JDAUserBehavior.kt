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
import dev.cubxity.kdp.behavior.UserBehavior
import dev.cubxity.kdp.engines.jda.entity.JDAUser
import dev.cubxity.kdp.engines.jda.util.await
import dev.cubxity.kdp.engines.jda.util.awaitOrNull
import dev.cubxity.kdp.entity.Snowflake
import dev.cubxity.kdp.exception.userNotFound
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse

open class JDAUserBehavior(
    override val kdp: KDP,
    override val id: Snowflake,
    private val jda: JDA
) : UserBehavior {
    override suspend fun asUser(): JDAUser {
        try {
            return JDAUser(kdp, jda.retrieveUserById(id.value).await())
        } catch (exception: ErrorResponseException) {
            throw when (exception.errorResponse) {
                ErrorResponse.UNKNOWN_USER -> userNotFound(id)
                else -> exception
            }
        }
    }

    override suspend fun asUserOrNull(): JDAUser? =
        jda.retrieveUserById(id.value).awaitOrNull()?.let { JDAUser(kdp, it) }
}