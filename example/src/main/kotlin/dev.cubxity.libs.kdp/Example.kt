/*
 *     KDP is a modular and customizable Discord command processing library.
 *     Copyright (C) 2020  Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.libs.kdp

import dev.cubxity.libs.kdp.admin.AdminModule
import dev.cubxity.libs.kdp.dsl.command
import dev.cubxity.libs.kdp.dsl.sub
import dev.cubxity.libs.kdp.module.Module
import dev.cubxity.libs.kdp.perms.PermissionDeniedException
import dev.cubxity.libs.kdp.perms.guildPermissions
import dev.cubxity.libs.kdp.perms.perms
import dev.cubxity.libs.kdp.processing.CommandException
import dev.cubxity.libs.kdp.processing.CommandProcessingPipeline
import dev.cubxity.libs.kdp.processing.MissingArgumentException
import dev.cubxity.libs.kdp.processing.processing
import dev.cubxity.libs.kdp.serialization.DefaultSerializationFactory
import dev.cubxity.libs.kdp.utils.embed.bold
import dev.cubxity.libs.kdp.utils.embed.buildEmbed
import dev.cubxity.libs.kdp.utils.embed.embed
import dev.cubxity.libs.kdp.utils.paginator.Paginator
import dev.cubxity.libs.kdp.utils.paginator.PaginatorEmbedInterface
import dev.cubxity.libs.kdp.utils.paginator.paginator
import dev.cubxity.libs.kdp.utils.sanitize
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import java.awt.Color

class ExampleModule(kdp: KDP) : Module(kdp, "example") {
    companion object {
        val example by command("example|ex|e <user>", "Example command")
        val sanitize by example.sub("sanitize|s <msg...>", "Echos a message back")
        val embedCommand by example.sub("embed|e <msg...>", "Embed a message")
        val paginate by example.sub("paginate", "Lorem ipsum")
        val ban by example.sub("ban", "Permission check for ban")
    }

    init {
        example {
            handler {
                val user: User = args.receive("user")
                send("You are referring to ${user.asTag}!")
            }
        }
        sanitize {
            handler {
                val msg: String = args.receive("msg")
                val sanitized = msg.sanitize(this)
                send("You said $sanitized!")
            }
        }
        embedCommand {
            handler {
                val msg: String = args.receive("msg")
                val embed = embed {
                    title = "Echo"

                    field("Message") { +msg }
                    field("Sent by") { +executor }
                }
                send(embed)
            }
        }
        paginate {
            handler {
                val embed = buildEmbed { title = "Lorem ipsum" }
                val paginator = paginator {
                    LOREM_IPSUM.lines().forEach { +it }
                }
                PaginatorEmbedInterface(paginator, embed).sendTo(this)
            }
        }
        ban {
            guildPermissions = listOf(Permission.BAN_MEMBERS)
            handler {
                send("Congrats! You have the force to ban people.")
            }
        }
    }
}

fun main() {
    val kdp = kdp {
        +ExampleModule::class
        +AdminModule::class

        processing {
            prefix = "^"
        }

        perms {
            admins = listOf("290921387655430144")
        }

        intercept(CommandProcessingPipeline.PRE_FILTER) {
            if (context.channel.id != "485175582854873132") finish()
        }

        intercept(CommandProcessingPipeline.ERROR) {
            val ex = context.exception ?: return@intercept
            val embed = embed {
                color = Color.RED

                when (ex) {
                    is CommandException -> {
                        +":x: "
                        +ex.message!!
                    }
                    is MissingArgumentException -> {
                        +":x: "
                        +"Missing required argument: "
                        +ex.arg.bold
                    }
                    is PermissionDeniedException -> {
                        +":x: "
                        +"Permission denied. "
                        if (ex.missingPermissions != null)
                            +"Missing guild permissions."
                    }
                    else -> {
                        title = "Error"
                        +ex.toString()
                    }
                }
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