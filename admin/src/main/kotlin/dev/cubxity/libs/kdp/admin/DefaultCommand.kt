package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.utils.embed.codeBlock
import dev.cubxity.libs.kdp.utils.embed.embed

fun AdminModule.default() = AdminModule.admin {
    botAdmin = true

    handler {
        val embed = embed {
            title = "Admin module"
            +"Available commands\n"
            +codeBlock { +subCommands.joinToString { it.name } }
        }
        send(embed)
    }
}