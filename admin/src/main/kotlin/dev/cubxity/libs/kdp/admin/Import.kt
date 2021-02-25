package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin

fun AdminModule.import() = AdminModule.import {
    botAdmin = true
    handler {
        val importString: String = args.receive("imports")
        val importList = importString.split(",")
        importList.forEach { imports.add(it) }
        send("Added import${if (importList.size > 1) "s" else ""}")
    }
}