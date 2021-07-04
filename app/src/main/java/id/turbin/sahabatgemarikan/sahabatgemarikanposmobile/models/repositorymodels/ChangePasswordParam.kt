package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ChangePasswordRequest

data class ChangePasswordParam(
    val token: String,
    val changePasswordRequest: ChangePasswordRequest
)
