package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName

data class Unit(
        @SerializedName(value = "id") val id: Long,
        @SerializedName(value = "name") val name: String,
)
