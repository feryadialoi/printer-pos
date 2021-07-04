package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class Sale(
  @SerializedName(value = "id") var id: Long,
  @SerializedName(value = "code") var code: String,
  @SerializedName(value = "transaction_date") var transactionDate: String,
  @SerializedName(value = "created_at") var createdAt: String,
  @SerializedName(value = "grandtotal") var grandTotal: BigDecimal,
  @SerializedName(value = "total_item") var totalItem: Int
)