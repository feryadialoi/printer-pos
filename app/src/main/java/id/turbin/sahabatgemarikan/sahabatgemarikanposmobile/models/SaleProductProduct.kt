package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class SaleProductProduct(
    @SerializedName(value = "id") val id: Long,
    @SerializedName(value = "name") val name: String,
    @SerializedName(value = "basic_price") val basicPrice: BigDecimal
)
