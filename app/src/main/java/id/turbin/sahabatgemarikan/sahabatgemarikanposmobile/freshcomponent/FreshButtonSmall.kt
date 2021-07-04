package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshButtonSmallLayoutBinding

class FreshButtonSmall(private val viewBinding: FreshButtonSmallLayoutBinding) {
    // views and layouts
    private val textView: TextView = viewBinding.tvFreshButtonSmallText
    private val cardView: MaterialCardView = viewBinding.cvFreshButtonSmall

    fun setText(text: String): FreshButtonSmall {
        textView.text = text
        return this
    }

    fun setTextSize(textSize: Float): FreshButtonSmall {
        textView.textSize = textSize
        return this
    }

    fun setOnClickListener(onClick: () -> Unit): FreshButtonSmall {
        viewBinding.root.setOnClickListener { onClick() }
        return this
    }

}


