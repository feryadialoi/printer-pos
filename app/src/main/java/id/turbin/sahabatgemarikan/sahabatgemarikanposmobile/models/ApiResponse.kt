package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName

data class ApiResponse<T> (
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "data") val data: T
)
