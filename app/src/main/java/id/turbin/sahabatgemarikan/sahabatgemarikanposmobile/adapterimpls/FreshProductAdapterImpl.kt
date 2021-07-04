package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.ProductAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshProductCardLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshProductCard
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil


class FreshProductAdapterImpl(
    products: List<Product>,
    onAdd: (Product, Int) -> Unit,
    onDecrement: (Product, Int) -> Unit,
    onIncrement: (Product, Int) -> Unit,
    onEditText: (Int, Int) -> Unit
) : ProductAdapter(products, onAdd, onDecrement, onIncrement, onEditText) {

    class ViewHolder(val viewBinding: FreshProductCardLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBinding: FreshProductCardLayoutBinding = FreshProductCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding = viewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder

        val product = products[position]
        FreshProductCard(viewBinding = holder.viewBinding)
            .setProductName(name = product.name)
            .setProductPrice(price = StringUtil.currencyFormat(product.basicPrice))
            .setProductUnit(unit = "")
            .setProductQuantity(quantity = product.quantity.toString())
            .setDecrementOnClickListener { onDecrement(product, position) }
            .setIncrementOnClickListener { onIncrement(product, position) }
            .setAddOnClickListener { onAdd(product, position) }
            .setImage(product.image)

        if (product.quantity == 0) {
            FreshProductCard(viewBinding = holder.viewBinding)
                .toggleShowButton(toggleShowButton = FreshProductCard.ToggleShowButton.ADD_BUTTON)
        } else {
            FreshProductCard(viewBinding = holder.viewBinding)
                .toggleShowButton(toggleShowButton = FreshProductCard.ToggleShowButton.COUNTER_BUTTON)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun addProducts(products: List<Product>) {
        this.products = products
    }
}
