/*
 *     KDP is a modular and customizable Discord command processing library.
 *     Copyright (C) 2020  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp.perms

import dev.cubxity.libs.kdp.KDP
import dev.cubxity.libs.kdp.command.Command
import dev.cubxity.libs.kdp.feature.KDPFeature
import dev.cubxity.libs.kdp.feature.install
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline
import net.dv8tion.jda.api.Permission

class PermsFeature(kdp: KDP) {
    /**
     * List of bot admins
     */
    var admins: List<String> = emptyList()

    /**
     * Admin bypasses guild permission check
     */
    var adminBypass: Boolean = true

    init {
        kdp.intercept(CommandProcessingPipeline.POST_FILTER) {
            val cmd = context.command ?: return@intercept
            val g = context.guild
            val isAdmin = context.executor.id in admins
            val botAdmin = cmd.flags[FLAG_BOT_ADMIN] as? Boolean ?: false
            if (botAdmin && !isAdmin) {
                context.exception = PermissionDeniedException()
                kdp.execute(context, CommandProcessingPipeline.ERROR)
                finish()
                return@intercept
            }

            val guildPerms = cmd.flags[FLAG_GUILD_PERMISSIONS] as? List<Permission>
            if (g != null && guildPerms != null && (!adminBypass || !isAdmin)) {
                val m = g.getMember(context.executor)
                if (m == null) {
                    finish()
                    return@intercept
                }
                val missingPermissions = guildPerms.filter { it !in m.permissions }
                if (missingPermissions.isNotEmpty()) {
                    context.exception = PermissionDeniedException(missingPermissions)
                    kdp.execute(context, CommandProcessingPipeline.ERROR)
                    finish()
                    return@intercept
                }
            }
        }
    }

    companion object Feature : KDPFeature<KDP, PermsFeature, PermsFeature> {
        override val key = "kdp.features.perms"
        const val FLAG_GUILD_PERMISSIONS = "perms:guild_permissions"
        const val FLAG_BOT_ADMIN = "perms:bot_admin"

        override fun install(pipeline: KDP, configure: PermsFeature.() -> Unit) =
            PermsFeature(pipeline).apply(configure)
    }
}

/**
 * Get or install [PermsFeature] feature and run [opt] on it
 */
fun KDP.perms(opt: PermsFeature.() -> Unit = {}): PermsFeature = (features[PermsFeature.key] as PermsFeature?
    ?: install(PermsFeature)).apply(opt)

var Command.guildPermissions: List<Permission>
    get() = flags[PermsFeature.FLAG_GUILD_PERMISSIONS] as? List<Permission> ?: emptyList()
    set(value) {
        flags[PermsFeature.FLAG_GUILD_PERMISSIONS] = value
    }

var Command.botAdmin: Boolean
    get() = flags[PermsFeature.FLAG_BOT_ADMIN] as? Boolean ?: false
    set(value) {
        flags[PermsFeature.FLAG_BOT_ADMIN] = value
    }