package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.widget.TextView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshSaleProductCardLayoutBinding

class FreshSaleProductCard(private val viewBinding: FreshSaleProductCardLayoutBinding) {
    // views
    private val nameTextView: TextView = viewBinding.tvSaleProductCardName
    private val priceXQuantityTextView: TextView = viewBinding.tvSaleProductCardPriceXQuantity
    private val subtotalTextView: TextView = viewBinding.tvSaleProductCardSubtotal


    fun setProductName(name: String): FreshSaleProductCard {
        nameTextView.text = name
        return this
    }

    fun setPriceXQuantity(priceXQuantity: String): FreshSaleProductCard {
        priceXQuantityTextView.text = priceXQuantity
        return this
    }

    fun setSubtotal(subtotal: String): FreshSaleProductCard {
        subtotalTextView.text = subtotal
        return this
    }

    fun setOnClickListener(onClick: () -> Unit): FreshSaleProductCard {
        viewBinding.root.setOnClickListener {
            onClick()
        }
        return this
    }
}