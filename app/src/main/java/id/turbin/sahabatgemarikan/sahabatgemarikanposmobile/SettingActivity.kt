package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivitySettingBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshAlertDialogDangerLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshAlertDialogDanger
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshButton
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Printer
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.properties.BluetoothProperty
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.PrinterSharedPreference
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.ByteArrayUtil
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.PrintDataUtil
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.PrinterViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : BaseActivity() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var printerSharedPreference: PrinterSharedPreference

    @Inject
    lateinit var printerViewModel: PrinterViewModel

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var bluetoothDevice: BluetoothDevice

    private lateinit var bluetoothConnectProgressDialog: ProgressDialog
    private lateinit var viewBinding: ActivitySettingBinding

    companion object {
        @JvmStatic
        private val TAG = "SettingActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySettingBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_setting)
        setContentView(viewBinding.root)

        initView()

        onLoadPrinter()

        observePrinter()
    }

    private fun observePrinter() {
        printerViewModel.printer.observe(this) { printer ->
            deletePrinterListener(printer = printer)
            printerNameAndAddressListener(printer = printer)
        }
    }

    private fun onLoadPrinter() {
        val printer = printerSharedPreference.getPrinter()
        if (printer != null) {
            printerViewModel.setPrinter(printer = printer)
        } else {
            printerViewModel.setPrinter(printer = Printer(name = "", address = ""))
        }
    }

    private fun deletePrinterListener(printer: Printer) {
        if (printer.name == "" && printer.address == "") {
            viewBinding.ibtnSettingDeleteSavedPrinter.visibility = View.INVISIBLE
        } else {
            viewBinding.ibtnSettingDeleteSavedPrinter.visibility = View.VISIBLE
        }
    }

    private fun printerNameAndAddressListener(printer: Printer) {
        if (printer.name == "" && printer.address == "") {
            viewBinding.tvConnectedPrinterName.text = "Tidak ada printer"
            viewBinding.tvConnectedPrinterAddress.text = "Tidak ada printer"
        } else {
            viewBinding.tvConnectedPrinterName.text = printer.name
            viewBinding.tvConnectedPrinterAddress.text = printer.address
        }
    }

    private fun initView() {
        initSelectPrinterButton()
        initTestPrintButton()
        initToolbar()
        initDeletePrinterButton()
    }

    private fun initSelectPrinterButton() {
        FreshButton(viewBinding = viewBinding.btnFreshSettingSelectPrinter)
            .setText(text = "Pilih Printer")
            .setOnClickListener { selectPrinterToConnect() }
    }

    private fun initTestPrintButton() {
        FreshButton(viewBinding = viewBinding.btnFreshSettingTestPrint)
            .setText(text = "Tes Print")
            .setOnClickListener { testPrint() }
    }

    private fun initDeletePrinterButton() {
        viewBinding.ibtnSettingDeleteSavedPrinter.setOnClickListener { deleteSavedPrinter() }
    }

    private fun deleteSavedPrinter() {
        FreshAlertDialogDanger(viewBinding = FreshAlertDialogDangerLayoutBinding.inflate(layoutInflater))
            .setAlertDialogText("Anda yakin hapus printer yang tersimpan ?")
            .setButtonYesText("Hapus")
            .setButtonYesOnClickListener {
                printerSharedPreference.removePrinter()
                printerViewModel.setPrinter(Printer(name = "", address = ""))
            }
            .setButtonNoText("Batal")
            .create()
            .show()

    }

    private fun selectPrinterToConnect() {
        if (!bluetoothAdapter.isEnabled) {
            enableBluetooth()
        } else {
            selectPrinterFromPrinterList()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.tbActivitySetting)
        supportActionBar.apply {
            title = "Pengaturan"
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun testPrint() {
        if (!bluetoothAdapter.isEnabled) {
            enableBluetoothAndTestPrint()
        } else {
            bluetoothIsEnabled(isTestPrint = true)
        }
    }

    private fun enableBluetooth() {
        router.enableBluetooth(activity = this, requestCode = BluetoothProperty.REQUEST_ENABLE_BLUETOOTH)
    }

    private fun enableBluetoothAndTestPrint() {
        router.enableBluetooth(activity = this, requestCode = BluetoothProperty.REQUEST_ENABLE_BLUETOOTH_AND_TEST_PRINT)
    }

    private fun bluetoothIsEnabled(isTestPrint: Boolean = false) {
        val printer = printerSharedPreference.getPrinter()
        if(printer != null) {
            connectingPrinter(printer = printer, isSelectPrinterWhenFailedToConnect = true, isTestPrint = true)
        } else {
            if (isTestPrint) {
                selectPrinterFromPrinterListAndTestPrint()
            } else{
                selectPrinterFromPrinterList()
            }
        }
    }

    private fun connectingPrinter(printer: Printer, isSelectPrinterWhenFailedToConnect: Boolean = false, isTestPrint: Boolean = false) {
        try {
            val sahabatGemarikanPosMobileApplication = application as SahabatGemarikanPosMobileApplication

            val bluetoothDevice = bluetoothAdapter.getRemoteDevice(printer.address)

            sahabatGemarikanPosMobileApplication.bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(BluetoothProperty.APPLICATION_UUID)

            showProgressDialog(printer)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    bluetoothAdapter.cancelDiscovery()
                    sahabatGemarikanPosMobileApplication.bluetoothSocket.connect()

                    withContext(Dispatchers.Main) {
                        bluetoothConnectProgressDialog.dismiss()
                        Toast.makeText(this@SettingActivity, "Printer terhubung", Toast.LENGTH_SHORT).show()
                    }

                    if (isTestPrint) {
                        testPrinting()
                    } else {
                        sahabatGemarikanPosMobileApplication.bluetoothSocket.close()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "GlobalScope.launch(Dispatchers.IO).connectingPrinter: $e")
                    withContext(Dispatchers.Main) {
                        bluetoothConnectProgressDialog.dismiss()
                        Toast.makeText(this@SettingActivity, "Gagal menghubungkan printer", Toast.LENGTH_SHORT).show()
                        if (isSelectPrinterWhenFailedToConnect) {
                            selectPrinterFromPrinterList()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "connectingPrinter: $e")
        }
    }

    private fun showProgressDialog(printer: Printer) {
        bluetoothConnectProgressDialog = ProgressDialog.show(
            this,
            "Menghubungkan",
            "${printer.name} : ${printer.address}",
            true,
            false
        )
    }

    private fun selectPrinterFromPrinterListAndTestPrint() {
        router.navigateToListPrinterActivityForResult(
            activity = this,
            requestCode = BluetoothProperty.REQUEST_CONNECT_DEVICE_AND_TEST_PRINT
        )
    }

    private fun selectPrinterFromPrinterList() {
        router.navigateToListPrinterActivityForResult(
            activity = this,
            requestCode = BluetoothProperty.REQUEST_CONNECT_DEVICE
        )
    }

    private fun testPrinting() = runBlocking {
        try {
            val sahabatGemarikanPosMobileApplication = application as SahabatGemarikanPosMobileApplication
            val outputStream = sahabatGemarikanPosMobileApplication.bluetoothSocket.outputStream

            val bill = "Sahabat Gemarikan POS Mobile".padStart(30, ' ') + "\n"
            outputStream.write(bill.toByteArray())

            val qrCodeString = "https://sahabatgemarikan.id"
            val qrCodeBitmap = PrintDataUtil.generateQRCodeToBitmap(
                text = qrCodeString,
                imageWidth = 200,
                imageHeight = 200
            )

            val qrCodeByteArray = ByteArrayUtil.bitmapToByteArray(bitmap = qrCodeBitmap)
            outputStream.write(qrCodeByteArray)

            val bottomSpace = "\n\n"
            outputStream.write(bottomSpace.toByteArray())

            sahabatGemarikanPosMobileApplication.bluetoothSocket.close()

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult(requestCode: $requestCode, resultCode: $resultCode)")
        when (requestCode) {
            BluetoothProperty.REQUEST_CONNECT_DEVICE -> {
                onActivityResultRequestConnectDevice(requestCode, resultCode, data)
            }
            BluetoothProperty.REQUEST_ENABLE_BLUETOOTH -> {
                onActivityResultRequestEnableBluetooth(requestCode, resultCode, data)
            }
            BluetoothProperty.REQUEST_CONNECT_DEVICE_AND_TEST_PRINT -> {
                onActivityResultRequestConnectDeviceAndTestPrint(requestCode, resultCode, data)
            }
            BluetoothProperty.REQUEST_ENABLE_BLUETOOTH_AND_TEST_PRINT -> {
                onActivityResultRequestEnableBluetoothAndTestPrint(requestCode, resultCode, data)
            }
            else -> {

            }
        }
    }

    private fun onActivityResultRequestEnableBluetoothAndTestPrint(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResultRequestEnableBluetoothAndTestPrint(resultCode: $resultCode)")
        if (resultCode == Activity.RESULT_OK) {
            bluetoothIsEnabled(isTestPrint = true)
        }
    }

    private fun onActivityResultRequestConnectDeviceAndTestPrint(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResultRequestConnectDeviceAndTestPrint(resultCode: $resultCode)")
        if (resultCode == Activity.RESULT_OK) {
            val printer = Printer(
                name = data?.extras?.getString("name") ?: "",
                address = data?.extras?.getString("address") ?: ""
            )

            printerSharedPreference.setPrinter(printer)
            printerViewModel.setPrinter(printer)

            connectingPrinter(printer = printer, isTestPrint = true)
        }
    }

    private fun onActivityResultRequestConnectDevice(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResultRequestConnectDevice(resultCode: $resultCode)")
        if (resultCode == Activity.RESULT_OK) {
            val printer = Printer(
                name = data?.extras?.getString("name") ?: "",
                address = data?.extras?.getString("address") ?: ""
            )

            printerSharedPreference.setPrinter(printer)
            printerViewModel.setPrinter(printer)

            connectingPrinter(printer = printer)
        }
    }

    private fun onActivityResultRequestEnableBluetooth(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResultRequestEnableBluetooth(resultCode: $resultCode)")
        if (resultCode == Activity.RESULT_OK) {
            bluetoothIsEnabled()
        }
    }
}