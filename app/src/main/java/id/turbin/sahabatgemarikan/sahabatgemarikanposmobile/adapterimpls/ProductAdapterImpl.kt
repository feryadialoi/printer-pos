package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.ProductAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ProductItemLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil

class ProductAdapterImpl(
    private var products: List<Product>,
    val onAdd: (product: Product, index: Int) -> Unit,
    val onDecrement: (product: Product, index: Int) -> Unit,
    val onIncrement: (product: Product, index: Int) -> Unit,
    val onEditText: (index: Int, quantity: Int) -> Unit
): RecyclerView.Adapter<ProductAdapterImpl.ViewHolder>() {
    class ViewHolder(val binding: ProductItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProductItemLayoutBinding = ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvProductItemName.text = products[position].name
        holder.binding.tvProductItemBasicPrice.text = StringUtil.currencyFormat(products[position].basicPrice)

        Glide.with(holder.itemView.context)
            .load(products[position].image)
            .centerCrop()
            //.placeholder(R.drawable)
            .into(holder.binding.ivProductItemImage)


        if (products[position].quantity == 0) {
            holder.binding.clProductItemAddButton.visibility = View.VISIBLE
            holder.binding.clProductItemCounter.visibility = View.GONE
            println("VISIBLE")
        } else {
            holder.binding.clProductItemAddButton.visibility = View.GONE
            holder.binding.clProductItemCounter.visibility = View.VISIBLE
            println("GONE")
        }

        holder.binding.btnProductIncrement.setOnClickListener {
            onIncrement(products[position], position)
        }

        holder.binding.btnProductDecrement.setOnClickListener {
            onDecrement(products[position], position)
        }

        holder.binding.btnProductItemAddProduct.setOnClickListener {
            onAdd(products[position], position)
        }

        holder.binding.etProductQuantity.setText(products[position].quantity.toString())

        holder.binding.etProductQuantity.addTextChangedListener(onTextChanged = {text: CharSequence?, start: Int, before: Int, count: Int ->
//            onEditText(position, text.toString().toInt())
        })
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun addProducts(products: List<Product>) {
        this.products = products
    }

}