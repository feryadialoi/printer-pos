package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Printer
import javax.inject.Inject

class PrinterViewModel @Inject constructor(): ViewModel() {
    var printers = MutableLiveData<MutableList<Printer>>()

    var printer: MutableLiveData<Printer> = MutableLiveData<Printer>()

    init {
        printer.postValue(Printer(name = "", address = ""))
    }

    fun loadPrinter() {

    }

    fun setPrinter(printer: Printer) {
        this.printer.postValue(printer)
    }

    fun setPrinters(printers: MutableList<Printer>) {
        this.printers.postValue(printers)
    }
}