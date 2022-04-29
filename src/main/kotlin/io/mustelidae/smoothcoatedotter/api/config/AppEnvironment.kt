package io.mustelidae.smoothcoatedotter.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppEnvironment {

    open class ConnInfo {
        var host: String = "localhost"
        var timeout: Long = 2
        var logging: Boolean = false
        var useDummy: Boolean = false
        var perRoute: Int = 50
        var connTotal: Int = 500
        var connLiveDuration: Long = 60
    }
}
