package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters

import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleProduct

abstract class SaleProductAdapter(
    protected var saleProducts: MutableList<SaleProduct>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract fun addSaleProducts(saleProducts: MutableList<SaleProduct>)
}