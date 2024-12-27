package io.mustelidae.smoothcoatedotter.api.lock

import io.mustelidae.smoothcoatedotter.api.config.redis.RedisKey

class LockRedisKey(
    private val userId: String,
) : RedisKey {
    override fun getKey(): String = KEY + userId

    companion object {
        private const val KEY = "${RedisKey.PREFIX}:lock:user:"
    }
}
