package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Printer
import javax.inject.Inject

class PrinterSharedPreference @Inject constructor(
    @ApplicationContext context: Context
) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val PRINTER_NAME = "printer_name"
        private const val PRINTER_ADDRESS = "printer_address"
    }

    fun setPrinter(printer: Printer) {
        setPrinterName(printerName = printer.name)
        setPrinterAddress(printerAddress = printer.address)
    }

    fun getPrinter(): Printer? {
        val printerAddress = getPrinterAddress()

        if (printerAddress == "") {
            return null
        }

        return Printer(
            name = getPrinterName(),
            address = getPrinterAddress()
        )
    }

    fun removePrinter() {
        removePrinterName()
        removePrinterAddress()
    }

    private fun removePrinterName() {
        preferences.edit().remove(PRINTER_NAME).apply()
    }

    private fun removePrinterAddress() {
        preferences.edit().remove(PRINTER_ADDRESS).apply()
    }

    private fun getPrinterName(): String {
        return preferences.getString(PRINTER_NAME, "")!!
    }

    private fun getPrinterAddress(): String {
        return preferences.getString(PRINTER_ADDRESS, "")!!
    }

    private fun setPrinterName(printerName: String) {
        preferences.edit().putString(PRINTER_NAME, printerName).apply()
    }

    private fun setPrinterAddress(printerAddress: String) {
        preferences.edit().putString(PRINTER_ADDRESS, printerAddress).apply()
    }


}