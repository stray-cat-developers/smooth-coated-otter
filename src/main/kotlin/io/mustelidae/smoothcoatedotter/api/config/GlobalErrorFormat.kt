package io.mustelidae.smoothcoatedotter.api.config

import io.mustelidae.smoothcoatedotter.api.common.ErrorCode
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Boilerplate.Error")
data class GlobalErrorFormat(
    @Schema(description = "error occurred time")
    val timestamp: String,
    @Schema(description = "error code")
    val code: ErrorCode,
    @Schema(description = "text displayed to the user")
    val description: String? = null,
    @Schema(description = "exception message")
    val message: String,
    @Schema(description = "exception name")
    val type: String,
    @Schema(description = "reference error code")
    val refCode: String? = null,
    @Schema(description = "stack trace")
    val trace: String? = null
)
