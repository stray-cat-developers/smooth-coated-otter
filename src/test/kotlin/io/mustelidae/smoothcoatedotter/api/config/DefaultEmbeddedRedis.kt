package io.mustelidae.smoothcoatedotter.api.config

import io.mustelidae.smoothcoatedotter.api.common.Constant
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Lazy(false)
@TestConfiguration
class DefaultEmbeddedRedis(
    private val properties: RedisProperties
) {

    @Value("\${embedded-redis.port:-1}")
    var port: Int = 0

    @Bean
    fun redisClusterConfiguration(): RedisClusterConfiguration {
        return RedisClusterConfiguration(
            listOf("${properties.host}:$port")
        )
    }

    @Bean(name = [Constant.Redis.USER_LOCK])
    fun userLockRedisTemplate(): StringRedisTemplate {
        val configuration = RedisStandaloneConfiguration(properties.host, port)
        val factory = LettuceConnectionFactory(configuration).apply {
            afterPropertiesSet()
        }

        return StringRedisTemplate().apply {
            setConnectionFactory(factory)
        }
    }
}
