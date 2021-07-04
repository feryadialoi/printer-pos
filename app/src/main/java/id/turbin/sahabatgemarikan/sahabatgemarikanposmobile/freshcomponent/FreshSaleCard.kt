package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.widget.TextView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshSaleCardLayoutBinding

class FreshSaleCard(private val viewBinding: FreshSaleCardLayoutBinding) {
    // views
    private val codeTextView: TextView = viewBinding.tvFreshSaleCardCode
    private val itemCountTextView: TextView = viewBinding.tvFreshSaleCardItemCount
    private val subtotalTextView: TextView = viewBinding.tvFreshSaleCardSubtotal
    private val dateTextView: TextView = viewBinding.tvSaleCardDate

    fun setCode(code: String): FreshSaleCard {
        codeTextView.text = code
        return this
    }

    fun setItemCount(itemCount: String): FreshSaleCard {
        itemCountTextView.text = itemCount
        return this
    }

    fun setSubtotal(subtotal: String): FreshSaleCard {
        subtotalTextView.text = subtotal
        return this
    }

    fun setDate(date: String): FreshSaleCard {
        dateTextView.text = date
        return this
    }

    fun setOnClickListener(onClick: () -> Unit): FreshSaleCard {
        viewBinding.root.setOnClickListener {
            onClick()
        }
        return this
    }

}