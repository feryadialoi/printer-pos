package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshProductCartCardLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.UnitUtil

class FreshProductCartCard(private val viewBinding: FreshProductCartCardLayoutBinding) {
    // views
    private val productNameTextView: TextView = viewBinding.tvFreshProductCartCardName
    private val productQuantityEditText: EditText = viewBinding.etFreshProductCartCardQuantity
    private val productUnitTextView: TextView = viewBinding.tvFreshProductCartCardUnit
    private val productPriceTextView: TextView = viewBinding.tvFreshProductCartCardPrice
    private val productImageImageView: ImageView = viewBinding.ivFreshProductCartCardImage
    private val productSubtotalTextView: TextView = viewBinding.tvFreshProductCartCardSubtotal
    private val productIncrementButton: FreshButtonSmall = FreshButtonSmall(viewBinding = viewBinding.btnFreshProductCartCardIncrement)
    private val productDecrementButton: FreshButtonSmall = FreshButtonSmall(viewBinding = viewBinding.btnFreshProductCartCardDecrement)

    fun setProductName(name: String): FreshProductCartCard {
        productNameTextView.text = name
        return this
    }

    fun setProductPrice(price: String): FreshProductCartCard {
        productPriceTextView.text = price
        return this
    }

    fun setProductUnit(unit: String): FreshProductCartCard {
        productUnitTextView.text = unit
        return this
    }

    fun setProductQuantity(quantity: String): FreshProductCartCard {
        productQuantityEditText.setText(quantity)
        return this
    }

    fun setProductSubtotal(subtotal: String): FreshProductCartCard {
        productSubtotalTextView.text = subtotal
        return this
    }

    fun setProductImage(imageUrl: String): FreshProductCartCard {

        Glide.with(viewBinding.root.context)
            .load(imageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(UnitUtil.dpToPx(12))))
            .into(productImageImageView)

        return this
    }

    fun setProductIncrementOnClickListener(onClick: () -> Unit ): FreshProductCartCard {
        productIncrementButton
            .setText("+")
            .setTextSize(20F)
            .setOnClickListener { onClick() }

        return this
    }

    fun setProductDecrementOnClickListener(onClick: () -> Unit): FreshProductCartCard {
        productDecrementButton
            .setText("-")
            .setTextSize(20F)
            .setOnClickListener { onClick() }

        return this
    }

    fun setOnClick(onClick: () -> Unit): FreshProductCartCard {
        viewBinding.root.setOnClickListener { onClick() }
        return this
    }
}