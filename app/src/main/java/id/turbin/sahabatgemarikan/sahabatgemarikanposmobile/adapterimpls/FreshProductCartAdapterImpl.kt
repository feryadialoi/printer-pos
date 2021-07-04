package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.LoginActivity
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.ProductCartAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshProductCartCardLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshProductCartCard
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil
import java.math.BigDecimal

class FreshProductCartAdapterImpl(
    products: List<Product>,
    onIncrement: (Product, Int) -> Unit,
    onDecrement: (Product, Int) -> Unit
) : ProductCartAdapter(products, onIncrement, onDecrement) {


    class ViewHolder(val viewBinding: FreshProductCartCardLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)

    override fun addProductCarts(products: List<Product>) {
        this.products = products
    }

    override fun incrementProductCartQuantity(product: Product, index: Int) {
        onIncrement(product, index)
    }

    override fun decrementProductCartQuantity(product: Product, index: Int) {
        onDecrement(product, index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBinding: FreshProductCartCardLayoutBinding = FreshProductCartCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding = viewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder

        val product = products[position]

        FreshProductCartCard(viewBinding = holder.viewBinding)
            .setProductName(name = product.name)
            .setProductPrice(price = StringUtil.currencyFormat(product.basicPrice))
            .setProductUnit(unit = "")
            .setProductQuantity(quantity = product.quantity.toString())
            .setProductSubtotal(subtotal = StringUtil.currencyFormat(product.basicPrice.multiply(
                BigDecimal(product.quantity)
            )))
            .setProductImage(imageUrl = product.image)
            .setOnClick { Log.d(LoginActivity.TAG, "on click") }
            .setProductIncrementOnClickListener { incrementProductCartQuantity(product = product, index = position) }
            .setProductDecrementOnClickListener { decrementProductCartQuantity(product = product, index = position) }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}