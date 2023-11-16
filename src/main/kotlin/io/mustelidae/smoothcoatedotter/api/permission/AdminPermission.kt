package io.mustelidae.smoothcoatedotter.api.permission

class AdminPermission(
    private val id: Long,
) : Permission {
    override fun isValid(): Boolean = true
}
