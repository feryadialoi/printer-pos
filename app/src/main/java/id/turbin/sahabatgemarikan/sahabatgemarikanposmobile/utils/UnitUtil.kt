package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils

import android.content.res.Resources

object UnitUtil {
    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }
}