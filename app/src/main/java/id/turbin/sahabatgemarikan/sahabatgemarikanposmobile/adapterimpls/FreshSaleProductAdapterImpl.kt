package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.LoginActivity
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.SaleProductAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshSaleProductCardLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshSaleProductCard
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleProduct
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil

class FreshSaleProductAdapterImpl(saleProducts: MutableList<SaleProduct>) : SaleProductAdapter(
    saleProducts
) {

    class ViewHolder(val viewBinding: FreshSaleProductCardLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)

    override fun addSaleProducts(saleProducts: MutableList<SaleProduct>) {
        this.saleProducts = saleProducts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBinding: FreshSaleProductCardLayoutBinding = FreshSaleProductCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding = viewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder

        val saleProduct = saleProducts[position]

        FreshSaleProductCard(viewBinding = holder.viewBinding)
            .setProductName(name = saleProduct.product.name)
            .setPriceXQuantity(priceXQuantity = "${StringUtil.currencyFormat(saleProduct.product.basicPrice)} x ${saleProduct.quantity}")
            .setSubtotal(subtotal = StringUtil.currencyFormat(saleProduct.total))
            .setOnClickListener {  }
    }

    override fun getItemCount(): Int {
        return saleProducts.size
    }
}