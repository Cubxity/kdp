plugins {
    kotlin("jvm") version "1.4.10" apply false
}

group = "dev.cubxity.kdp"
version = "2.0.0"

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }
}