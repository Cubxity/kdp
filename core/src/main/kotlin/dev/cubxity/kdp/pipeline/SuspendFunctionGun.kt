/*
 * Copyright 2014-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.cubxity.kdp.pipeline

import kotlinx.coroutines.*
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*

internal class SuspendFunctionGun<TSubject : Any, TContext : Any>(
    initialSubject: TSubject,
    override val context: TContext,
    private val blocks: List<PipelineInterceptor<TSubject, TContext>>
) : PipelineContext<TSubject, TContext>, PipelineExecutor<TSubject>, CoroutineScope {

    override val coroutineContext: CoroutineContext get() = continuation.context

    // Stack-walking state
    private var lastPeekedIndex: Int = -1

    private var rootContinuation: Any? = null
    private var index = 0

    // this is impossible to inline because of property name clash
    // between PipelineContext.context and Continuation.context
    private val continuation: Continuation<Unit> = object : Continuation<Unit> {
        @Suppress("UNCHECKED_CAST")
        override val context: CoroutineContext
            get() = when (val cont = rootContinuation) {
                null -> throw IllegalStateException("Not started")
                is Continuation<*> -> cont.context
                is List<*> -> (cont as List<Continuation<*>>).last().context
                else -> throw IllegalStateException("Unexpected rootContinuation value")
            }

        override fun resumeWith(result: Result<Unit>) {
            if (result.isFailure) {
                resumeRootWith(Result.failure(result.exceptionOrNull()!!))
                return
            }

            loop(false)
        }
    }

    override var subject: TSubject = initialSubject
        private set

    override fun finish() {
        index = blocks.size
    }

    override suspend fun proceed(): TSubject = suspendCoroutineUninterceptedOrReturn { continuation ->
        if (index == blocks.size) return@suspendCoroutineUninterceptedOrReturn subject

        addContinuation(continuation)

        if (loop(true)) {
            discardLastRootContinuation()
            return@suspendCoroutineUninterceptedOrReturn subject
        }

        COROUTINE_SUSPENDED
    }

    override suspend fun proceedWith(subject: TSubject): TSubject {
        this.subject = subject
        return proceed()
    }

    override suspend fun execute(initial: TSubject): TSubject {
        index = 0
        if (index == blocks.size) return initial
        subject = initial

        if (rootContinuation != null) throw IllegalStateException("Already started")

        return proceed()
    }

    /**
     * @return `true` if it is possible to return result immediately
     */
    private fun loop(direct: Boolean): Boolean {
        do {
            val index = index  // it is important to read index every time
            if (index == blocks.size) {
                if (!direct) {
                    resumeRootWith(Result.success(subject))
                    return false
                }

                return true
            }

            this@SuspendFunctionGun.index = index + 1  // it is important to increase it before function invocation

            @Suppress("UNCHECKED_CAST")
            val next =
                blocks[index] as Function3<PipelineContext<TSubject, TContext>, TSubject, Continuation<Unit>, Any?>

            try {
                val me = this@SuspendFunctionGun

                val rc = next.invoke(me, me.subject, me.continuation)
                if (rc === COROUTINE_SUSPENDED) {
                    return false
                }
            } catch (cause: Throwable) {
                resumeRootWith(Result.failure(cause))
                return false
            }
        } while (true)
    }

    private fun resumeRootWith(result: Result<TSubject>) {
        val rootContinuation = rootContinuation

        @Suppress("UNCHECKED_CAST")
        val next = when (rootContinuation) {
            null -> throw IllegalStateException("No more continuations to resume")
            is Continuation<*> -> {
                this.rootContinuation = null
                lastPeekedIndex = -1
                rootContinuation
            }
            is ArrayList<*> -> {
                if (rootContinuation.isEmpty()) throw IllegalStateException("No more continuations to resume")
                lastPeekedIndex = rootContinuation.lastIndex - 1
                rootContinuation.removeAt(rootContinuation.lastIndex)
            }
            else -> unexpectedRootContinuationValue(rootContinuation)
        } as Continuation<TSubject>

        if (!result.isFailure) {
            next.resumeWith(result)
        } else {
            next.resumeWithException(result.exceptionOrNull()!!)
        }
    }

    private fun discardLastRootContinuation() {
        val rootContinuation = rootContinuation

        @Suppress("UNCHECKED_CAST")
        when (rootContinuation) {
            null -> throw IllegalStateException("No more continuations to resume")
            is Continuation<*> -> {
                lastPeekedIndex = -1
                this.rootContinuation = null
            }
            is ArrayList<*> -> {
                if (rootContinuation.isEmpty()) throw IllegalStateException("No more continuations to resume")
                rootContinuation.removeAt(rootContinuation.lastIndex)
                lastPeekedIndex = rootContinuation.lastIndex
            }
            else -> unexpectedRootContinuationValue(rootContinuation)
        }
    }

    private fun addContinuation(continuation: Continuation<TSubject>) {
        when (val rootContinuation = rootContinuation) {
            null -> {
                lastPeekedIndex = 0
                this.rootContinuation = continuation
            }
            is Continuation<*> -> {
                this.rootContinuation = ArrayList<Continuation<*>>(blocks.size).apply {
                    add(rootContinuation)
                    add(continuation)
                    lastPeekedIndex = 1
                }
            }
            is ArrayList<*> -> {
                @Suppress("UNCHECKED_CAST")
                rootContinuation as ArrayList<Continuation<TSubject>>
                rootContinuation.add(continuation)
                lastPeekedIndex = rootContinuation.lastIndex
            }
            else -> unexpectedRootContinuationValue(rootContinuation)
        }
    }

    private fun unexpectedRootContinuationValue(rootContinuation: Any?): Nothing =
        throw IllegalStateException("Unexpected rootContinuation content: $rootContinuation")
}
