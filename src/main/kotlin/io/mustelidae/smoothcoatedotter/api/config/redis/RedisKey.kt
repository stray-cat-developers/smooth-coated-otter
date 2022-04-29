package io.mustelidae.smoothcoatedotter.api.config.redis

interface RedisKey {
    fun getKey(): String

    companion object {
        const val PREFIX = "smooth-coated-otter"
    }
}
