package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters


import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product

abstract class ProductAdapter(
    protected var products: List<Product>,
    val onAdd: (product: Product, index: Int) -> Unit,
    val onDecrement: (product: Product, index: Int) -> Unit,
    val onIncrement: (product: Product, index: Int) -> Unit,
    val onEditText: (index: Int, quantity: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract fun addProducts(products: List<Product>)
}