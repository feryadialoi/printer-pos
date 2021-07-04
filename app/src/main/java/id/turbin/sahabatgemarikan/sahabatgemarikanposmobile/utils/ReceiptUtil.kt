package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.R
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleDetail
import java.math.BigDecimal

object ReceiptUtil {
    fun formatReceipt(saleDetail: SaleDetail): String {
        var receipt = ""

        // header start
        receipt = receipt + "\n" + "Sahabat Gemarikan".padStart(24, ' ') + "\n\n"
        receipt = receipt + "Kode Transaksi" + saleDetail.code.padStart(18, ' ') + "\n"
        receipt = receipt + "".padEnd(32, '-') + "\n"

        // date start
        receipt = receipt + StringUtil.dateFormat(saleDetail.createdAt) + "\n"
        // date end

        // header end

        receipt = receipt + "".padEnd(32, '-') + "\n"

        // list item start
        saleDetail.products.forEach {
            receipt = receipt + it.product.name + "\n"
            receipt =
                receipt + "${it.quantity} x @ ${StringUtil.currencyFormat(it.product.basicPrice)}" + "\n"
            receipt =
                receipt + StringUtil.currencyFormat(it.product.basicPrice.multiply(BigDecimal(it.quantity))) + "\n"
        }
        // list item end

        receipt = receipt + "".padEnd(32, '-') + "\n"

        // total start
        receipt += "\n"
        receipt = receipt + "Total" + StringUtil.currencyFormat(saleDetail.grandTotal)
            .padStart(27, ' ') + "\n"
        receipt += "\n" + "\n"
        // total end

        return receipt
    }

    fun formatQRCode(saleDetail: SaleDetail): Bitmap {
        // val qrCodeString = saleDetail.code
        val qrCodeString = "https://sahabatgemarikan.id"

        return PrintDataUtil.generateQRCodeToBitmap(
            text = qrCodeString,
            imageHeight = 200,
            imageWidth = 200
        )
    }

    fun formatLogoReceipt(resources: Resources): Bitmap {
        return BitmapFactory.decodeResource(resources, R.drawable.logo_sahabat_gemarikan_receipt,
            BitmapFactory.Options().apply {
            inScaled = false
        })
    }
}