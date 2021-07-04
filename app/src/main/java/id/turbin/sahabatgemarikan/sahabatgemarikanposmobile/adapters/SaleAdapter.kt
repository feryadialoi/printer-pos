package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters

import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Sale

abstract class SaleAdapter(
    protected var sales: MutableList<Sale>,
    val onClick: (sale: Sale, index: Int) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract fun addSales(sales: MutableList<Sale>)

}