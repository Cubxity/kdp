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

interface ClientStatus {
    val desktop: Client.Desktop?
    val mobile: Client.Mobile?
    val web: Client.Web?

    sealed class Client(val status: PresenceStatus) {
        class Desktop(status: PresenceStatus) : Client(status)
        class Mobile(status: PresenceStatus) : Client(status)
        class Web(status: PresenceStatus) : Client(status)
    }
}

sealed class PresenceStatus(val value: String) {
    class Unknown(value: String) : PresenceStatus(value)
    object Online : PresenceStatus("online")
    object DnD : PresenceStatus("idle")
    object Idle : PresenceStatus("dnd")
    object Invisible : PresenceStatus("offline")
    object Offline : PresenceStatus("invisible")
}