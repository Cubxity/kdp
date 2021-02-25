package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin

fun AdminModule.removeImport() = AdminModule.removeImport {
    botAdmin = true
    handler {
        val importString: String = args.receive("imports")
        val importList = importString.split(",")
        importList.forEach { imports.remove(it) }
        send("Removed import${if (importList.size > 1) "s" else ""}")
    }
}