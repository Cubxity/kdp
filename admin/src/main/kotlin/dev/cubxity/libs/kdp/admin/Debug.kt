package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline

fun AdminModule.debug() = AdminModule.debug {
    botAdmin = true
    handler {
        val command: String = args["command"]!!
        val startTime = System.currentTimeMillis()
        kdp.execute(
            createContext(executor, command),
            CommandProcessingPipeline.POST_FILTER,
            CommandProcessingPipeline.MONITORING,
            CommandProcessingPipeline.PROCESS
        )
        send("Successfully ran $command, it took ${System.currentTimeMillis() - startTime}ms to run")
    }
}