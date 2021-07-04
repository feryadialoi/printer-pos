package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshProductCardLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.UnitUtil

class FreshProductCard(private val viewBinding: FreshProductCardLayoutBinding) {

    // views
    private val productNameTextView: TextView = viewBinding.tvFreshProductCardProductName
    private val productPriceTextView: TextView = viewBinding.tvFreshProductCardProductPrice
    private val productUnitTextView: TextView = viewBinding.tvFreshProductCardProductUnit
    private val productQuantityEditText: EditText = viewBinding.etFreshProductCardProductQuantity
    private val productAddButton: FreshButtonSmall = FreshButtonSmall(viewBinding = viewBinding.btnFreshAddProduct)
    private val productDecrementButton: FreshButtonSmall = FreshButtonSmall(viewBinding = viewBinding.btnFreshDecrement)
    private val productIncrementButton: FreshButtonSmall = FreshButtonSmall(viewBinding = viewBinding.btnFreshIncrement)


    fun setProductName(name: String): FreshProductCard {
        productNameTextView.text = name
        return this
    }

    fun setProductPrice(price: String): FreshProductCard {
        productPriceTextView.text = price
        return this
    }

    fun setProductUnit(unit: String): FreshProductCard {
        productUnitTextView.text = unit
        return this
    }

    fun setProductQuantity(quantity: String): FreshProductCard {
        productQuantityEditText.setText(quantity)
        return this
    }

    fun setAddOnClickListener(onClick: () -> Unit): FreshProductCard {
        productAddButton
            .setText("Tambah")
            .setOnClickListener { onClick() }
        return this
    }

    fun setIncrementOnClickListener(onClick: () -> Unit): FreshProductCard {
        productIncrementButton
            .setText("+")
            .setOnClickListener { onClick() }
        return this
    }

    fun setDecrementOnClickListener(onClick: () -> Unit): FreshProductCard {
        productDecrementButton
            .setText("-")
            .setOnClickListener { onClick() }
        return this
    }

    fun toggleShowButton(toggleShowButton: ToggleShowButton): FreshProductCard {
        if (toggleShowButton == ToggleShowButton.ADD_BUTTON) {
            viewBinding.llProductCardAddButton.visibility = View.VISIBLE
            viewBinding.llProductCardCounterButton.visibility = View.INVISIBLE
            productDecrementButton
                .setTextSize(14F)

            productIncrementButton
                .setTextSize(14F)
        } else {
            viewBinding.llProductCardAddButton.visibility = View.INVISIBLE
            viewBinding.llProductCardCounterButton.visibility = View.VISIBLE

            productDecrementButton
                .setTextSize(20F)

            productIncrementButton
                .setTextSize(20F)
        }
        return this
    }

    fun setImage(imageUrl: String): FreshProductCard {
        Glide.with(viewBinding.root.context)
            .load(imageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(UnitUtil.dpToPx(12))))
            .into(viewBinding.ivFreshProductCardImage)

        return this
    }

    enum class ToggleShowButton {
        ADD_BUTTON, COUNTER_BUTTON
    }
}