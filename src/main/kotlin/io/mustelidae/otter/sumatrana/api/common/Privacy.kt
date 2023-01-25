package io.mustelidae.otter.sumatrana.api.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Common.Privacy")
data class Privacy(
    val location: io.mustelidae.otter.sumatrana.api.common.Location? = null,
    val vehicle: io.mustelidae.otter.sumatrana.api.common.Vehicle? = null
)
