package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls.PrinterAdapterImpl
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivityListPrinterBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Printer
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.PrinterSharedPreference
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.PrinterViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ListPrinterActivity : AppCompatActivity() {

    @Inject
    lateinit var printerViewModel: PrinterViewModel

    @Inject
    lateinit var printerSharedPreferences: PrinterSharedPreference

    private lateinit var viewBinding: ActivityListPrinterBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var printerAdapter: PrinterAdapterImpl

    companion object {
        @JvmStatic
        private val TAG = "ListPrinterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityListPrinterBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_list_printer)
        setContentView(viewBinding.root)

        setResult(Activity.RESULT_CANCELED)

        initView()

        // init bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices = bluetoothAdapter.bondedDevices.map { bluetoothDevice ->
            Printer(
                name = bluetoothDevice.name,
                address = bluetoothDevice.address
            )
        }



        if (pairedDevices.isNotEmpty()) {
            printerAdapter.apply {
                this.addPrinters(printers = pairedDevices as MutableList<Printer>)
                this.notifyDataSetChanged()

            }
        } else {
            Log.d(TAG, "no devices")
        }

        viewBinding.pbListPrinterLoading.visibility = View.GONE
    }

    private fun initView() {
        initToolbar()
        initPrinterRecyclerView()
    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.tbListPrinterActivity)
        supportActionBar.apply {
            title = "Daftar Perangkat"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun initPrinterRecyclerView() {
        viewBinding.rvListPrinterPrinter.layoutManager = LinearLayoutManager(this)
        printerAdapter = PrinterAdapterImpl(
            printers = mutableListOf(),
            onClick = { printer, index ->
                onClickPrinterItem(printer = printer, index = index)
            }
        )
        viewBinding.rvListPrinterPrinter.adapter = printerAdapter
    }

    private fun onClickPrinterItem(printer: Printer, index: Int) {
        try {
            bluetoothAdapter.cancelDiscovery()

            goBack(printer = printer)

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun goBack(printer: Printer) {
        val bundle = Bundle()

        bundle.putString("name", printer.name)
        bundle.putString("address", printer.address)

        val backIntent = Intent()
        backIntent.putExtras(bundle)

        setResult(Activity.RESULT_OK, backIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter.cancelDiscovery()
    }
}