# KDP
**K**otlin **D**iscord **P**rocessing library.

## Example
View full example [here](example/src/main/kotlin/dev.cubxity.libs.kdp/)
```kotlin
class ExampleModule(kdp: KDP) : Module(kdp, "example") {
    companion object {
        val example by command("example|ex <user>", "Example command")
    }

    init {
        example {
            handler {
                val user: User = args["user"] ?: error("User not found")
                send("You are referring to ${user.asTag}!")
            }
        }
    }
}

fun main() {
    val kdp = kdp {
        +ExampleModule::class

        processing {
            prefix = "^"
        }

        intercept(CommandProcessingPipeline.ERROR) {
            val embed = embed {
                color = Color.RED
                title = "Error"
                +context.exception?.toString()
            }
            context.send(embed)
        }

        serializationFactory = DefaultSerializationFactory()

        init()
    }
    JDABuilder()
        .setEventManager(kdp.manager)
        .setToken(System.getProperty("token"))
        .build()
}
```