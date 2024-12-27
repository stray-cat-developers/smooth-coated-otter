package io.mustelidae.smoothcoatedotter.api.utils

import java.util.concurrent.atomic.AtomicLong

object FixtureID {
    private val atomicLong = AtomicLong(1)
    private val atomicUserId = AtomicLong(1000)

    fun inc(): Long = atomicLong.getAndIncrement()

    fun userId() = atomicUserId.getAndIncrement()
}
