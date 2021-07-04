package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName

data class CreateSaleRequest(
    @SerializedName(value = "products") val createSaleRequestProducts: List<CreateSaleRequestProduct>,
)
