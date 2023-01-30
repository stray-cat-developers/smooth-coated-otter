package io.mustelidae.otter.sumatrana.api.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Common.FuelType")
enum class FuelType(val desc: String) {
    GASOLINE("휘발유"),
    ADVANCED_GASOLINE("고급 휘발유"),
    DIESEL("디젤"),
    KEROSENE("등유"),
    HYDROGEN("수소"),
    BUTANE("LPG")
}
