package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.content.res.ColorStateList
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.R
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshButtonSmallLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshButtonSmallOutlinedLayoutBinding

class FreshButtonSmallOutlined(private val viewBinding: FreshButtonSmallOutlinedLayoutBinding) {
    // views and layouts
    private val textView: TextView = viewBinding.tvFreshButtonSmallOutlinedText
    private val cardView: MaterialCardView = viewBinding.cvFreshButtonSmallOutlined

    fun setText(text: String): FreshButtonSmallOutlined {
        textView.text = text
        return this
    }

    fun setOnClickListener(onClick: () -> Unit): FreshButtonSmallOutlined {
        viewBinding.root.setOnClickListener { onClick() }
        return this
    }
}


