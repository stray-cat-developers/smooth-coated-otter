package io.mustelidae.smoothcoatedotter.api.config

import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfiguration {

    @Bean
    fun default(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("API")
        .addOpenApiCustomiser {
            it.info.version("v1")
            it.components.schemas.putAll(ModelConverters.getInstance().read(GlobalErrorFormat::class.java))
        }
        .packagesToScan("io.mustelidae.otter.neotropical.api.domain")
        .pathsToMatch("/v1/**")
        .pathsToExclude("/v1/maintenance/**", "/v1/migration/**", "/v1/bridge/**")
        .build()

    @Bean
    fun maintenance(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Maintenance")
        .addOpenApiCustomiser {
            it.info.version("v1")
            it.components.schemas.putAll(ModelConverters.getInstance().read(GlobalErrorFormat::class.java))
        }
        .packagesToScan("io.mustelidae.otter.neotropical.api.domain")
        .pathsToMatch("/v1/maintenance/**")
        .build()

    @Bean
    fun migration(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Migration")
        .addOpenApiCustomiser {
            it.info.version("v1")
            it.components.schemas.putAll(ModelConverters.getInstance().read(GlobalErrorFormat::class.java))
        }
        .packagesToScan("io.mustelidae.otter.neotropical.api.domain")
        .pathsToMatch("/v1/migration/**")
        .build()

    @Bean
    fun bridge(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Bridge")
        .addOpenApiCustomiser {
            it.info.version("v1")
            it.components.schemas.putAll(ModelConverters.getInstance().read(GlobalErrorFormat::class.java))
        }
        .packagesToScan("io.mustelidae.otter.neotropical.api.domain")
        .pathsToMatch("/v1/bridge/**")
        .build()

    @Bean
    fun uiConfig(): OpenAPI = OpenAPI().components(
        Components().addSchemas(
            "GlobalError", ModelConverters.getInstance().resolveAsResolvedSchema(AnnotatedType(GlobalErrorFormat::class.java)).schema
        )
    )
}

@Schema(name = "Common.Error")
data class GlobalErrorFormat(
    val timestamp: String,
    @Schema(description = "Http Status Code")
    val status: Int,
    @Schema(description = "error code")
    val code: String,
    @Schema(description = "text displayed to the user")
    val description: String? = null,
    @Schema(description = "exception message")
    val message: String,
    @Schema(description = "exception name")
    val type: String,
    @Schema(description = "reference error code")
    val refCode: String? = null
)
