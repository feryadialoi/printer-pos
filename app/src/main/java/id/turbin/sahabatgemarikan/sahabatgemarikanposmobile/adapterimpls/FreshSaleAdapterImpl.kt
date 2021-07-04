package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.SaleAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshSaleCardLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshSaleCard
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Sale
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil

class FreshSaleAdapterImpl(
    sales: MutableList<Sale>,
    onClick: (Sale, Int) -> Unit
) : SaleAdapter(sales, onClick) {

    class ViewHolder(val viewBinding: FreshSaleCardLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)

    override fun addSales(sales: MutableList<Sale>) {
        this.sales = sales
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBinding: FreshSaleCardLayoutBinding = FreshSaleCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding = viewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder

        val sale = sales[position]

        FreshSaleCard(viewBinding = holder.viewBinding)
            .setCode(code = sale.code)
            .setItemCount(itemCount = "${sale.totalItem} barang")
            .setSubtotal(subtotal = StringUtil.currencyFormat(sale.grandTotal))
            .setDate(date = sale.transactionDate)
            .setOnClickListener { onClick(sale, position) }
    }

    override fun getItemCount(): Int {
        return sales.size
    }
}