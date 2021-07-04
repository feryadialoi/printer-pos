package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Product(
        @SerializedName(value = "id") var id: Long,
        @SerializedName(value = "code") var code: String,
        @SerializedName(value = "name") var name: String,
        @SerializedName(value = "default_unit_id") var defaultUnitId: Long,
        @SerializedName(value = "default_unit") var defaultUnit: ProductUnit,
        @SerializedName(value = "image") var image: String,
        @SerializedName(value = "basic_price") var basicPrice: BigDecimal,
        @SerializedName(value = "qty_ready") var quantityReady: Int,
        @SerializedName(value = "qty_total") var quantityTotal: Int,
        var quantity: Int = 0
)
