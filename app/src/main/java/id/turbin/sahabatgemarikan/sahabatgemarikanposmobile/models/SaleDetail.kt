package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

class SaleDetail(
    @SerializedName(value = "id") val id: Long,
    @SerializedName(value = "is_done") val isDone: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "post_date") val postDate: String,
    @SerializedName(value = "transaction_date") val transactionDate: String,
    @SerializedName(value = "warehouse_id") val warehouseId: Long,
    @SerializedName(value = "warehouse") val warehouse: SaleWarehouse,
    @SerializedName(value = "customer_id") val customerId: Long,
    @SerializedName(value = "customer") val customer: SaleCustomer,
    @SerializedName(value = "due_date") val dueDate: String,
    @SerializedName(value = "customer_address") val customerAddress: String,
    @SerializedName(value = "shipping_address") val shippingAddress: String,
    @SerializedName(value = "contact_name") val contactName: String,
    @SerializedName(value = "contact_number") val contactNumber: String,
    @SerializedName(value = "total_gross") val totalGross: BigDecimal,
    @SerializedName(value = "note") val note: String,
    @SerializedName(value = "is_discount_percent") val isDiscountPercent: Boolean,
    @SerializedName(value = "discount_percent") val discountPercent: Double,
    @SerializedName(value = "discount_amount") val discountAmount: BigDecimal,
    @SerializedName(value = "inclusive_tax") val inclusiveTax: BigDecimal,
    @SerializedName(value = "exclusive_tax") val exclusiveTax: BigDecimal,
    @SerializedName(value = "inclusive_fee") val inclusiveFee: BigDecimal,
    @SerializedName(value = "grandtotal") val grandTotal: BigDecimal,
    @SerializedName(value = "created_at") val createdAt: String,
    @SerializedName(value = "products") val products: List<SaleProduct>
) {
}