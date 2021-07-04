package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.SaleProductItemLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleProduct
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil

class SaleProductAdapterImpl(
        private var saleProducts: MutableList<SaleProduct>
) : RecyclerView.Adapter<SaleProductAdapterImpl.ViewHolder>() {
    class ViewHolder(val binding: SaleProductItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SaleProductItemLayoutBinding = SaleProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvSaleProductName.text = saleProducts[position].product.name
        holder.binding.tvSaleProductQuantity.text = "x"+ saleProducts[position].quantity.toString()
        holder.binding.tvSaleProductPrice.text = StringUtil.currencyFormat(saleProducts[position].product.basicPrice)
    }

    override fun getItemCount(): Int {
        return saleProducts.size
    }

    fun addSaleProducts(saleProducts: MutableList<SaleProduct>) {
        this.saleProducts = saleProducts
    }
}