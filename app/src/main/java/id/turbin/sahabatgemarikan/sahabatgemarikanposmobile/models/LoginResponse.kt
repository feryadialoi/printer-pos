package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName(value = "user") var user: User,
    @SerializedName(value = "token") var token: String,
)