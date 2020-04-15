package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin
import net.dv8tion.jda.api.entities.User

fun AdminModule.dm() = AdminModule.dm {
    botAdmin = true
    handler {
        val user: User = args["user"] ?: executor
        val message: String = args["message"]!!
        user.openPrivateChannel().queue { it.sendMessage(message).queue() }
    }
}