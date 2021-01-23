/*
 *  KDP is a modular and customizable Discord command processing library.
 *  Copyright (C) 2020-2021 Cubxity.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.cubxity.kdp.entity

enum class Permission(val code: Long) {
    CreateInstantInvite(0x00000001),
    KickMembers(0x00000002),
    BanMembers(0x00000004),
    Administrator(0x00000008),
    ManageChannels(0x00000010),
    ManageGuild(0x00000020),
    AddReactions(0x00000040),
    ViewAuditLog(0x00000080),
    ViewChannel(0x00000400),
    SendMessages(0x00000800),
    SendTTSMessages(0x00001000),
    ManageMessages(0x00002000),
    EmbedLinks(0x00004000),
    AttachFiles(0x00008000),
    ReadMessageHistory(0x00010000),
    MentionEveryone(0x00020000),
    UseExternalEmojis(0x00040000),
    ViewGuildInsights(0x00080000),
    Connect(0x00100000),
    Speak(0x00200000),
    MuteMembers(0x00400000),
    DeafenMembers(0x00800000),
    MoveMembers(0x01000000),
    UseVAD(0x02000000),
    PrioritySpeaker(0x00000100),
    ChangeNickname(0x04000000),
    ManageNicknames(0x08000000),
    ManageRoles(0x10000000),
    ManageWebhooks(0x20000000),
    ManageEmojis(0x40000000)
}

/**
 * A set of [permissions][Permission] in a guild.
 */
inline class Permissions(val code: Long) {
    val permissions: List<Permission>
        get() = Permission.values().filter { it in this }

    operator fun contains(permission: Permission): Boolean =
        code and permission.code != 0L

    operator fun plus(permission: Permission): Permissions =
        Permissions(code or permission.code)

    operator fun plus(permissions: Permissions): Permissions =
        Permissions(code or permissions.code)

    operator fun minus(permission: Permission): Permissions =
        Permissions(code xor permission.code)

    operator fun minus(permissions: Permissions): Permissions =
        Permissions(code xor permissions.code)

    companion object {
        inline val all: Permissions
            get() = invoke {
                Permission.values().forEach { +it }
            }

        inline val none: Permissions
            get() = Permissions(0)

        inline operator fun invoke(builder: Builder.() -> Unit = {}): Permissions =
            Builder().apply(builder).build()
    }

    class Builder(private var permissions: Permissions = none) {
        operator fun Permission.unaryPlus() {
            this@Builder.permissions = this@Builder.permissions + this
        }

        operator fun Permissions.unaryPlus() {
            this@Builder.permissions = this@Builder.permissions + this
        }

        operator fun Permission.unaryMinus() {
            this@Builder.permissions = this@Builder.permissions - this
        }

        operator fun Permissions.unaryMinus() {
            this@Builder.permissions = this@Builder.permissions - this
        }

        fun build(): Permissions = permissions
    }
}