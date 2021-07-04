package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations.Email
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations.Label
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations.Min
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations.NotEmpty

data class LoginRequest(

    @Label(value = "Email")
    @NotEmpty
    @Email
    @SerializedName(value = "email")
    val email: String,

    @Label(value = "Password")
    @NotEmpty
    @SerializedName(value = "password")
    val password: String,
)
