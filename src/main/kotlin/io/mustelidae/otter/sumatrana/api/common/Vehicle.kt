package io.mustelidae.otter.sumatrana.api.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Common.Vehicle")
data class Vehicle(
    val name: String,
    val platNumber: String,
    val fuelType: io.mustelidae.otter.sumatrana.api.common.FuelType? = null,
    val gradeId: Long? = null
)
