package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName

data class Profile(
        @SerializedName(value = "name") var name: String,
        @SerializedName(value = "email") var email: String,
        @SerializedName(value = "phone") var phone: String,
        @SerializedName(value = "address") var address: String,
        @SerializedName(value = "code") var code: String,
)