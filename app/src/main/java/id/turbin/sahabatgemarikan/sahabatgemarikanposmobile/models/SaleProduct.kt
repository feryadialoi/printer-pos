package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class SaleProduct(
    @SerializedName(value = "subject_to_tax") val subjectToTax: BigDecimal,
    @SerializedName(value = "disc1") val disc1: BigDecimal,
    @SerializedName(value = "disc2") val disc2: BigDecimal,
    @SerializedName(value = "unit_id") val unitId: Long,
    @SerializedName(value = "unit") val unit: Unit,
    @SerializedName(value = "qty") val quantity: Int,
    @SerializedName(value = "product_id") val productId: Long,
    @SerializedName(value = "product") val product: SaleProductProduct,
    @SerializedName(value = "total") val total: BigDecimal
)