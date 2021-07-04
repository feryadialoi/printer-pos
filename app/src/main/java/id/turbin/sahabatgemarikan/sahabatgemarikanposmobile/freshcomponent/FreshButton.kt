package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshButtonLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.UnitUtil

class FreshButton(private val viewBinding: FreshButtonLayoutBinding) {
    // views
    private var buttonText: TextView = viewBinding.freshButtonText
    private val freshButtonMaterialCardView: MaterialCardView = viewBinding.cvFreshButton

    fun setMargin(
        marginLeft: Int = 0,
        marginTop: Int = 0,
        marginRight: Int = 0,
        marginBottom: Int = 0
    ): FreshButton {
        val marginParams: ViewGroup.MarginLayoutParams =
            freshButtonMaterialCardView.layoutParams as ViewGroup.MarginLayoutParams
        marginParams.setMargins(
            UnitUtil.dpToPx(marginLeft),
            UnitUtil.dpToPx(marginTop),
            UnitUtil.dpToPx(marginRight),
            UnitUtil.dpToPx(marginBottom)
        )
        freshButtonMaterialCardView.requestLayout()

        return this
    }

    fun setIsEnable(isEnable: Boolean): FreshButton {
        freshButtonMaterialCardView.isEnabled = isEnable
        return this
    }

    fun setText(text: String): FreshButton {
        buttonText.text = text
        return this
    }

    fun setOnClickListener(onClick: () -> Unit): FreshButton {
        viewBinding.root.setOnClickListener {
            onClick()
        }

        return this
    }

}