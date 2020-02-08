# KDP
**K**otlin **D**iscord **P**rocessing library.

## Example
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
        ExampleModule::class.register()

        processing {
            prefix = "^"
        }

        intercept(CommandProcessingPipeline.ERROR) {
            val embed = EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Error")
                .setDescription(context.exception?.toString())
                .build()
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