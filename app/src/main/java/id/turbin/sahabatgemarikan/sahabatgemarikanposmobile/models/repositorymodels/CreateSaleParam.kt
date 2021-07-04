package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.CreateSaleRequest

data class CreateSaleParam(
    val token: String,
    val createSaleRequest: CreateSaleRequest
)
