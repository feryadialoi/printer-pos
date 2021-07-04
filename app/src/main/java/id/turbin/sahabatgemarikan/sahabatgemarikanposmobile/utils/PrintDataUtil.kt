package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

object PrintDataUtil {
    /**
     * * max canvasWidth is 384
     */
    fun generateQRCodeToBitmap(text: String, imageWidth: Int = 200, imageHeight: Int = 200): Bitmap {

        val canvasWidth = 384
        val canvasHeight = imageHeight

        val bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)

        val codeWriter = MultiFormatWriter()

        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, imageWidth, imageHeight)
            for (x in 0 until canvasWidth) {
                for (y in 0 until canvasHeight) {
                    bitmap.setPixel(x, y, Color.WHITE)
                }
            }

            // 384 - 200 = 184
            val widthDiff = canvasWidth - imageWidth
            // 184 / 2 = 92
            val xStart = widthDiff / 2
            // 92 + 200 = 292
            val width = xStart + imageWidth

            for (x in xStart until width) {
                for (y in 0 until imageHeight) {
                    bitmap.setPixel(x, y, if (bitMatrix[x - xStart, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.e("PrintDataUtil", e.toString())
        }

        return bitmap
    }
}