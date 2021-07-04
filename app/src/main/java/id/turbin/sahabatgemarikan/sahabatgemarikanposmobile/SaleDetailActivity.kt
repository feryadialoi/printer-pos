package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls.FreshSaleProductAdapterImpl
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.SaleProductAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivitySaleDetailBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshButton
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Printer
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleDetail
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleProduct
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.properties.BluetoothProperty
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.PrinterSharedPreference
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.ByteArrayUtil
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.PrintDataUtil
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.ReceiptUtil
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.SaleViewModel
import kotlinx.coroutines.*
import java.io.OutputStream
import java.math.BigDecimal
import javax.inject.Inject


@AndroidEntryPoint
class SaleDetailActivity : BaseActivity() {

    @Inject
    lateinit var saleViewModel: SaleViewModel

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    @Inject
    lateinit var printerSharedPreference: PrinterSharedPreference

    @Inject
    lateinit var router: Router

    private lateinit var viewBinding: ActivitySaleDetailBinding

    private var saleId: Long = 0

    private lateinit var saleProductAdapter: SaleProductAdapter

    private lateinit var bluetoothConnectProgressDialog: ProgressDialog

    companion object {
        @JvmStatic
        private val TAG = "SaleDetailActivity"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySaleDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        initView()

        getIntentExtra()

        loadSaleDetail()

        observeSaleDetail()
    }

    private fun toggleShowContent(isShow: Boolean) {
        if(isShow) {
            viewBinding.svSaleDetail.visibility = View.VISIBLE
            viewBinding.llSaleDetailFooter.visibility = View.VISIBLE
        } else {
            viewBinding.svSaleDetail.visibility = View.INVISIBLE
            viewBinding.llSaleDetailFooter.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeSaleDetail() {
        saleViewModel.saleDetail.observe(this) { saleDetail ->

            viewBinding.tvSaleDetailCode.text = saleDetail.code
            viewBinding.tvSaleDetailDate.text = saleDetail.createdAt

            saleProductAdapterListener(saleProducts = saleDetail.products as MutableList<SaleProduct>)

            viewBinding.tvSaleTotal.text = StringUtil.currencyFormat(saleDetail.grandTotal)
        }
    }

    private fun loadSaleDetail() {
        saleViewModel.loadSaleDetail(saleId = saleId).observe(this) { state ->
            when (state) {
                is LoadingResourceState -> {
                    onLoadSaleDetailLoading()
                }
                is SuccessResourceState<*> -> {
                    onLoadSaleDetailSuccess(saleDetail = (state.data as ApiResponse<*>).data as SaleDetail)
                }
                is ErrorResourceState -> {
                    onLoadSaleDetailError(error = state.error)
                }
            }
        }
    }

    private fun onLoadSaleDetailLoading() {
        viewBinding.pbSaleDetailGetSaleDetailLoading.visibility = View.VISIBLE
    }

    private fun onLoadSaleDetailSuccess(saleDetail: SaleDetail) {
        viewBinding.pbSaleDetailGetSaleDetailLoading.visibility = View.GONE
        toggleShowContent(true)
    }

    private fun onLoadSaleDetailError(error: Exception) {
        viewBinding.pbSaleDetailGetSaleDetailLoading.visibility = View.GONE
    }

    private fun getIntentExtra() {
        saleId = intent.getLongExtra("saleId", 0)
    }

    private fun initView() {
        toggleShowContent(false)
        initToolbar()
        initSaleDetailProductsRecyclerView()
        initButtonPrintReceipt()
    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.tbActivitySaleDetail)
        supportActionBar.apply {
            title = getString(R.string.sale_detail_activity_title)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }


    private fun initButtonPrintReceipt() {
        FreshButton(viewBinding = viewBinding.btnFreshSaleDetailPrintReceipt)
            .setText("Cetak Struk")
            .setOnClickListener { printReceipt() }
    }

    private fun printReceipt() {
        if (!bluetoothAdapter.isEnabled) {
            enableBluetooth()
        } else {
            bluetoothIsEnabled()
        }
    }

    private fun enableBluetooth() {
        router.enableBluetooth(activity = this, requestCode = BluetoothProperty.REQUEST_ENABLE_BLUETOOTH)
    }

    private fun bluetoothIsEnabled() {
        val printer = printerSharedPreference.getPrinter()
        if(printer != null) {
            connectingPrinter(printer, true)
        } else {
            selectPrinterFromPrinterList()
        }
    }

    private fun selectPrinterFromPrinterList() {
        router.navigateToListPrinterActivityForResult(
            activity = this,
            requestCode = BluetoothProperty.REQUEST_CONNECT_DEVICE
        )
    }

    private fun connectingPrinter(printer: Printer, isSelectPrinterWhenFailedToConnect: Boolean = false) {
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
                        Toast.makeText(this@SaleDetailActivity, "Printer terhubung", Toast.LENGTH_SHORT).show()
                    }

                    printingReceipt()
                } catch (e: Exception) {
                    Log.e(TAG, "GlobalScope.launch(Dispatchers.IO).connectingPrinter: $e")
                    withContext(Dispatchers.Main) {
                        bluetoothConnectProgressDialog.dismiss()
                        Toast.makeText(this@SaleDetailActivity, "Gagal menghubungkan printer", Toast.LENGTH_SHORT).show()
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
            else -> {

            }
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

            connectingPrinter(printer = printer)
        }
    }

    private fun onActivityResultRequestEnableBluetooth(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResultRequestEnableBluetooth(resultCode: $resultCode)")
        if (resultCode == Activity.RESULT_OK) {
            bluetoothIsEnabled()
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

    private fun printingReceipt() = runBlocking(Dispatchers.IO) {
        try {
            val sahabatGemarikanPosMobileApplication = application as SahabatGemarikanPosMobileApplication

            val outputStream: OutputStream = sahabatGemarikanPosMobileApplication.bluetoothSocket.outputStream

            val logo = ReceiptUtil.formatLogoReceipt(resources)
            outputStream.write(ByteArrayUtil.bitmapToByteArray(logo))

            val receipt = ReceiptUtil.formatReceipt(saleViewModel.saleDetail.value!!)
            outputStream.write(receipt.toByteArray())

            val qrCode = ReceiptUtil.formatQRCode(saleViewModel.saleDetail.value!!)
            outputStream.write(ByteArrayUtil.bitmapToByteArray(qrCode))

            val bottomSpace = "\n\n"
            outputStream.write(bottomSpace.toByteArray())

            sahabatGemarikanPosMobileApplication.bluetoothSocket.close()
        } catch (e: Exception) {
            Log.e(TAG, "printingReceipt: $e")
        }
    }

    private fun initSaleDetailProductsRecyclerView() {
        viewBinding.rvSaleSaleProduct.layoutManager = LinearLayoutManager(this)
        saleProductAdapter = FreshSaleProductAdapterImpl(saleProducts = mutableListOf())
        viewBinding.rvSaleSaleProduct.adapter = saleProductAdapter
    }

    private fun saleProductAdapterListener(saleProducts: MutableList<SaleProduct>) {
        saleProductAdapter.apply {
            this.addSaleProducts(saleProducts = saleProducts)
            this.notifyDataSetChanged()
        }
    }
}