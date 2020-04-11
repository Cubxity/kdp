package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline

fun AdminModule.repeat() = AdminModule.repeat {
    botAdmin = true
    handler {
        repeat(args["amount"]!!) {
            kdp.execute(
                createContext(executor, args["command"]!!),
                CommandProcessingPipeline.POST_FILTER,
                CommandProcessingPipeline.MONITORING,
                CommandProcessingPipeline.PROCESS
            )
        }
    }
}