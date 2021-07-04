package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters

import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product

abstract class ProductCartAdapter(
    protected var products: List<Product>,
    val onIncrement: (product: Product, index: Int) -> Unit,
    val onDecrement: (product: Product, index: Int) -> Unit,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract fun addProductCarts(products: List<Product>)

    protected abstract fun incrementProductCartQuantity(product: Product, index: Int)

    protected abstract fun decrementProductCartQuantity(product: Product, index: Int)
}