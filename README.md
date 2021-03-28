![KDP Banner](.github/assets/banner.svg)

[![Discord](https://img.shields.io/discord/802563482200440874?color=%237883d4&style=flat-square)](https://discord.gg/fqdaBkPbQ3)
[![License](https://img.shields.io/github/license/Cubxity/kdp?style=flat-square)](LICENSE)
[![Issues](https://img.shields.io/github/issues/Cubxity/kdp?style=flat-square)](https://github.com/Cubxity/kdp/issues)
[![Workflow Status](https://img.shields.io/github/workflow/status/Cubxity/kdp/gradle-ci/2.x?style=flat-square)](https://github.com/Cubxity/kdp/actions)

KDP is an asynchronous framework for creating Discord bots. Written 100% in [Kotlin](https://kotlinlang.org).

## Disclaimer

**KDP 2.x** is still in development stage, thus we can't guarantee API stability.

## Supported engines

- [JDA](https://github.com/DV8FromTheWorld/JDA) **Work-in-progress**
- [Kord](https://github.com/kordlib/kord) **Coming soon**

## Example

See [kdp-demo](kdp-demo) for more examples.

```kotlin
import dev.cubxity.kdp.engine.on
import dev.cubxity.kdp.engine.jda.JDA
import dev.cubxity.kdp.event.message.MessageCreateEvent
import dev.cubxity.kdp.kdp

suspend fun main() {
    kdp(JDA, "Your token here") {
        engine.on<MessageCreateEvent> {
            println("${message.author.username}: ${message.content}")
        }
    }.login()
}
```

## Credits

KDP is inspired by [Kord](https://github.com/kordlib/kord) and [Ktor](https://github.com/ktorio/ktor).