package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin

fun AdminModule.say() = AdminModule.say {
    botAdmin = true
    handler {
        val message: String = args["message"]!!
        send(message)
    }
}