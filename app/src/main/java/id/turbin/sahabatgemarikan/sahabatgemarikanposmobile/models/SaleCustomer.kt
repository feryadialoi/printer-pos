package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName

data class SaleCustomer(
    @SerializedName(value = "id") val id: Long,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "name") val name: String,
    @SerializedName(value = "address") val address: String,
)
