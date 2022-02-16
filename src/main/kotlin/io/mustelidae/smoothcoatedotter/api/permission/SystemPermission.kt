package io.mustelidae.smoothcoatedotter.api.permission

class SystemPermission(
    private val id: String
) : Permission {
    private var valid: Boolean = true

    override fun isValid(): Boolean = valid
}
