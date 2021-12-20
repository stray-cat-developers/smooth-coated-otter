package io.mustelidae.smoothcoatedotter.api.config.redis

interface RedisKey {
    fun getKey(): String
}