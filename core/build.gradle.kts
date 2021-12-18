/*
 * KDP is a modular and customizable Discord command processing library.
 * Copyright (C) 2020 Cubxity.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

dependencies {
    api(kotlin("stdlib-jdk8"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.5.2")
    api("net.dv8tion:JDA:5.0.0-alpha.2")
    api("com.github.MinnDevelopment:jda-reactor:1.5.0")
    api("org.apache.commons:commons-text:1.9")
    api("me.xdrop:fuzzywuzzy:1.3.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}