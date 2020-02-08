package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline

fun AdminModule.repeat() = AdminModule.repeat {
    botAdmin = true
    handler {
        val times: Int = args["amount"]!!
        for (i in 0..times) {
            val command: String = args["command"]!!
            val ctx = createContext(executor, command)
            kdp.execute(
                ctx,
                CommandProcessingPipeline.POST_FILTER,
                CommandProcessingPipeline.MONITORING,
                CommandProcessingPipeline.PROCESS
            )
        }
    }
}