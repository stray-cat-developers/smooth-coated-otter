package io.mustelidae.otter.sumatrana.api.common

class Error(
    errorCode: io.mustelidae.otter.sumatrana.api.common.ErrorCode,
    override val message: String,
    override var causeBy: Map<String, Any?>? = null
) : io.mustelidae.otter.sumatrana.api.common.ErrorSource {
    override val code = errorCode.name
    override var refCode: String? = null
}
