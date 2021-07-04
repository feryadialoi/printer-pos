package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ProductCartItemLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil

class ProductCartAdapterImpl(
        private var products: List<Product>,
        val onIncrement: (product: Product, index: Int) -> Unit,
        val onDecrement: (product: Product, index: Int) -> Unit,
) : RecyclerView.Adapter<ProductCartAdapterImpl.ViewHolder>() {
    class ViewHolder(val binding: ProductCartItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProductCartItemLayoutBinding = ProductCartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
                .load(products[position].image)
                .centerCrop()
                .into(holder.binding.ivProductCartItemImage)

        holder.binding.tvProductCartItemName.text = products[position].name
        holder.binding.tvProductCartItemPrice.text = StringUtil.currencyFormat(products[position].basicPrice)
        holder.binding.etProductCartItemQuantity.setText(products[position].quantity.toString())
        holder.binding.btnProductCartItemIncrement.setOnClickListener { incrementProductCartQuantity(product = products[position], index = position) }
        holder.binding.btnProductCartItemDecrement.setOnClickListener { decrementProductCartQuantity(product = products[position], index = position) }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun addProductCarts(products: List<Product>) {
        this.products = products
    }

    private fun incrementProductCartQuantity(product: Product, index: Int) {
        onIncrement(product, index)
    }

    private fun decrementProductCartQuantity(product: Product, index: Int) {
        onDecrement(product, index)
    }
}