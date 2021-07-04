package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName

data class ProductUnit(
    @SerializedName(value = "id") var id: Long,
    @SerializedName(value = "code") var code: String,
    @SerializedName(value = "name") var name: String,
)
