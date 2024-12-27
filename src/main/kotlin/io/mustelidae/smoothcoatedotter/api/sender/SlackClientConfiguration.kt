package io.mustelidae.smoothcoatedotter.api.sender

import io.mustelidae.smoothcoatedotter.api.config.AppEnvironment
import io.mustelidae.smoothcoatedotter.utils.RestClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SlackClientConfiguration(
    private val appEnvironment: AppEnvironment,
) {
    @Bean
    fun slackClient(): SlackClient {
        val env = appEnvironment.slack

        return if (env.useDummy) {
            DummySlackClient()
        } else {
            StableSlackClient(env, RestClient.new(env))
        }
    }
}
