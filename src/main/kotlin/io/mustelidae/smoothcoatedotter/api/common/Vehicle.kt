package io.mustelidae.smoothcoatedotter.api.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Common.Vehicle")
data class Vehicle(
    val name: String,
    val platNumber: String,
    val fuelType: FuelType? = null,
    val gradeId: Long? = null
)
