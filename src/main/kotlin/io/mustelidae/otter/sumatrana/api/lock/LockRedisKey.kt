package io.mustelidae.otter.sumatrana.api.lock

import io.mustelidae.otter.sumatrana.api.config.redis.RedisKey

class LockRedisKey(
    private val userId: String
) : RedisKey {

    override fun getKey(): String {
        return KEY + userId
    }

    companion object {
        private const val KEY = "${RedisKey.PREFIX}:lock:user:"
    }
}
