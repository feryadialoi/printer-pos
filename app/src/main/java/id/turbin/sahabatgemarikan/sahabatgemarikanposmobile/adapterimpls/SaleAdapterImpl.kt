package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.SaleItemLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Sale
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil

class SaleAdapterImpl(
        private var sales: MutableList<Sale>,
        val onClick: (sale: Sale, index: Int) -> Unit
): RecyclerView.Adapter<SaleAdapterImpl.ViewHolder>() {
    class ViewHolder(val binding: SaleItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SaleItemLayoutBinding = SaleItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvSaleItemTransactionCode.text = sales[position].code
        holder.binding.tvSaleItemDate.text = sales[position].transactionDate
        holder.binding.tvSaleItemTotal.text = StringUtil.currencyFormat(sales[position].grandTotal)
        holder.binding.tvSaleItemItemCount.text = sales[position].totalItem.toString() + " Barang"

        holder.itemView.setOnClickListener {
            onClick(sales[position], position)
        }
    }

    override fun getItemCount(): Int {
        return sales.size
    }

    fun addSales(sales: MutableList<Sale>) {
        this.sales = sales
    }
}