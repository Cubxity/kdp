package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.utils.await
import net.dv8tion.jda.api.entities.User

fun AdminModule.dm() = AdminModule.dm {
    botAdmin = true
    handler {
        val user: User = args.receiveOrNull("user") ?: executor
        val message: String = args.receive("message")
        val channel = user.openPrivateChannel().await()
        channel.sendMessage(message).await()
    }
}