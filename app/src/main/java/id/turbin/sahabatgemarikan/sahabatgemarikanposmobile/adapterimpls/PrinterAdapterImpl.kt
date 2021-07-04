package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.PrinterItemLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Printer

class PrinterAdapterImpl(
    private var printers: MutableList<Printer>,
    val onClick: (printer: Printer, index: Int) -> Unit
) : RecyclerView.Adapter<PrinterAdapterImpl.ViewHolder>() {
    class ViewHolder(val binding: PrinterItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: PrinterItemLayoutBinding = PrinterItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val printer = printers[position]
        holder.binding.tvPrinterItemName.text = printer.name
        holder.binding.tvPrinterItemAddress.text = printer.address
        holder.itemView.setOnClickListener {
            onClick(printer, position)
        }
    }

    override fun getItemCount(): Int {
        return printers.size
    }

    fun addPrinters(printers: MutableList<Printer>) {
        this.printers = printers
    }
}