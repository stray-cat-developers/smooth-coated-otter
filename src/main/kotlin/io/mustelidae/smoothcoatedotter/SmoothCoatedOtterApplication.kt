package io.mustelidae.smoothcoatedotter

import io.mustelidae.smoothcoatedotter.api.config.AppEnvironment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppEnvironment::class)
class SmoothCoatedOtterApplication

fun main(args: Array<String>) {
    runApplication<SmoothCoatedOtterApplication>(*args)
}