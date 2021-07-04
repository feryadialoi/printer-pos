package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName(value = "message") val message: String
)
