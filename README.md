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
                send("You are referring to ${user.asTag}! \uD83E\uDD80")
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

## Special Thanks
![](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with innovative and intelligent tools
for monitoring and profiling Java and .NET applications.
YourKit is the creator of <a href="https://www.yourkit.com/java/profiler/">YourKit Java Profiler</a>,
<a href="https://www.yourkit.com/.net/profiler/">YourKit .NET Profiler</a>,
and <a href="https://www.yourkit.com/youmonitor/">YourKit YouMonitor</a>
