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

package dev.cubxity.kdp.engine.jda.util

import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.concurrent.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend inline fun <T> RestAction<T>.await(): T =
    submit().await()

suspend inline fun <T> RestAction<T>.awaitOrNull(): T? =
    runCatching { submit().await() }.getOrNull()

suspend inline fun <T> Task<T>.await(): T = suspendCoroutine { cont ->
    onSuccess { cont.resume(it) }.onError { cont.resumeWithException(it) }
}

suspend inline fun <T> Task<T>.awaitOrNull(): T? = suspendCoroutine { cont ->
    onSuccess { cont.resume(it) }.onError { cont.resume(null) }
}