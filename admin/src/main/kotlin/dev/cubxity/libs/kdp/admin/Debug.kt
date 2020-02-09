package dev.cubxity.libs.kdp.admin

import dev.cubxity.libs.kdp.perms.botAdmin
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline

fun AdminModule.debug() = AdminModule.debug {
    botAdmin = true
    handler {
        val command: String = args["command"]!!
        val ctx = createContext(executor, command)
        val startTime = System.currentTimeMillis()
        kdp.execute(
            ctx,
            CommandProcessingPipeline.POST_FILTER,
            CommandProcessingPipeline.MONITORING,
            CommandProcessingPipeline.PROCESS
        )
        val endTime = System.currentTimeMillis()
        send("Successfully ran $command, it took ${endTime - startTime}ms to run")
    }
}