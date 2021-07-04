package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls.FreshProductCartAdapterImpl
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.ProductCartAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivitySaleBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshAlertDialogSuccessLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshAlertDialogSuccess
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshButton
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.*
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.StringUtil
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.ProductViewModel
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.SaleViewModel
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class SaleActivity : BaseActivity() {

    @Inject
    lateinit var productViewModel: ProductViewModel

    @Inject
    lateinit var saleViewModel: SaleViewModel

    @Inject
    lateinit var router: Router

    private lateinit var viewBinding: ActivitySaleBinding
    private lateinit var productCartAdapter: ProductCartAdapter

    companion object {
        @JvmStatic
        private val TAG = "SaleActivity"
    }

    private var isSaleDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySaleBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_sale)
        setContentView(viewBinding.root)

        initView()

        observeProducts()
    }

    private fun observeProducts() {
        productViewModel.products.observe(this) { products ->
            productCartAdapterListener(products = products)
            createSaleButtonListener(products = products)
            saleTotalListener(products = products)
            toggleShowNoContentListener(products = products)
        }
    }

    private fun toggleShowNoContentListener(products: MutableList<Product>) {
        val productsNotZeroQuantity = products.filter { product -> product.quantity > 0 }
        if (productsNotZeroQuantity.isEmpty()) {
            viewBinding.llSaleNoContent.visibility = View.VISIBLE
        } else {
            viewBinding.llSaleNoContent.visibility = View.GONE
        }
    }

    private fun productCartAdapterListener(products: MutableList<Product>) {
        productCartAdapter.apply {
            val notZeroQuantityOfProducts = products.filter {
                it.quantity > 0
            }
            Log.d(TAG, "Products" + notZeroQuantityOfProducts.toString())
            this.addProductCarts(products = notZeroQuantityOfProducts)
            this.notifyDataSetChanged()
        }
    }

    private fun initView() {
        initToolbar()
        initProductCartRecyclerView()
        initRefresh()
        initButtonCreateSale()
        initCreateSaleLoadingIndicator()
        initSwipeToRefresh()
        initNoContentView()

    }

    private fun initNoContentView() {
        viewBinding.btnFreshSaleBackToProduct.tvFreshButtonSmallText.text = "Tambah barang"
        viewBinding.btnFreshSaleBackToProduct.cvFreshButtonSmall.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initSwipeToRefresh() {
        viewBinding.srlSaleProductCartRefresh.setOnRefreshListener {
            onSwipeToRefresh()
        }
    }

    private fun initCreateSaleLoadingIndicator() {
        viewBinding.pbSaleCreateSaleLoading.visibility = View.GONE
    }

    private fun initButtonCreateSale() {
//        viewBinding.btnSaleCreateSale.text = getString(R.string.bt_sale_create_sale)
//        viewBinding.btnSaleCreateSale.setOnClickListener { createSale() }

        FreshButton(viewBinding = viewBinding.btnFreshSaleCreateSale)
            .setText(text = "Simpan Penjualan")
            .setOnClickListener { createSale() }
    }

    private fun createSaleButtonListener(products: List<Product>) {
        val notZeroQuantityOfProducts = products.filter { product ->
            product.quantity > 0
        }
//        viewBinding.btnSaleCreateSale.isEnabled = notZeroQuantityOfProducts.isNotEmpty()

        FreshButton(viewBinding = viewBinding.btnFreshSaleCreateSale)
            .setIsEnable(isEnable = notZeroQuantityOfProducts.isNotEmpty())

        viewBinding.llSaleCreateSale.visibility = if (notZeroQuantityOfProducts.isNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun saleTotalListener(products: List<Product>) {
        var total: BigDecimal = BigDecimal(0)
        products.forEach { product ->
            total += product.basicPrice.multiply(BigDecimal(product.quantity))
        }
        viewBinding.tvSaleTotal.text = StringUtil.currencyFormat(total)
    }

    private fun initRefresh() {
        viewBinding.srlSaleProductCartRefresh.setOnRefreshListener {
            onSwipeToRefresh()
        }

    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.tbActivitySale)
        supportActionBar.apply {
            title = getString(R.string.sale_activity_title)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun initProductCartRecyclerView() {
        viewBinding.rvSaleProductCart.layoutManager = LinearLayoutManager(this)
        productCartAdapter = FreshProductCartAdapterImpl(
            products = mutableListOf(),
            onIncrement = { product, index ->
                Log.d(TAG, "increment index : " + index)
                Log.d(TAG, "increment product : " + product)
                productViewModel.incrementProductQuantityOfProducts(
                    product = product,
                    index = index
                )
            },
            onDecrement = { product, index ->
                Log.d(TAG, "decrement index : " + index)
                Log.d(TAG, "decrement product : " + product)
                productViewModel.decrementProductQuantityOfProducts(
                    product = product,
                    index = index
                )
            }
        )
        viewBinding.rvSaleProductCart.adapter = productCartAdapter
    }

    private fun onSwipeToRefresh() {
        viewBinding.srlSaleProductCartRefresh.isRefreshing = false
    }

    private fun createSale() {
        val createSaleRequestProducts = productViewModel.products.value!!
            .filter {
                it.quantity > 0
            }
            .map { product ->
                CreateSaleRequestProduct(
                    subjectToTax = BigDecimal(0),
                    disc1 = BigDecimal(0),
                    disc2 = BigDecimal(0),
                    unitId = product.defaultUnitId,
                    quantity = product.quantity,
                    productId = product.id
                )
            }

        val createSaleRequest =
            CreateSaleRequest(createSaleRequestProducts = createSaleRequestProducts)

        saleViewModel.createSale(createSaleRequest = createSaleRequest).observe(this) { state ->
            when (state) {
                is LoadingResourceState -> {
                    onCreateSaleLoading()
                }
                is SuccessResourceState<*> -> {
                    onCreateSaleSuccess((state.data as ApiResponse<*>).data as SaleDetail)
                }
                is ErrorResourceState -> {
                    onCreateSaleError(error = state.error)
                }
            }
        }
    }

    private fun onCreateSaleLoading() {
        viewBinding.pbSaleCreateSaleLoading.visibility = View.VISIBLE
    }

    private fun onCreateSaleSuccess(saleDetail: SaleDetail) {
        viewBinding.pbSaleCreateSaleLoading.visibility = View.GONE
        productViewModel.loadProducts().observe(this) { state ->
            when (state) {
                is LoadingResourceState -> {
                    viewBinding.pbSaleCreateSaleLoading.visibility = View.VISIBLE
                }
                is SuccessResourceState<*> -> {
                    viewBinding.pbSaleCreateSaleLoading.visibility = View.GONE

                    isSaleDone = true

//                    AlertDialog.Builder(this)
//                        .setTitle("Penjualan")
//                        .setMessage("Penjualan berhasil disimpan")
//                        .setPositiveButton("Lihat Detil") { dialog, which ->
//                            dialog.dismiss()
//                            router.navigateToSaleDetailActivityAndFinish(
//                                activity = this,
//                                saleId = saleDetail.id
//                            )
//                        }
//                        .create()
//                        .show()

                    FreshAlertDialogSuccess(viewBinding = FreshAlertDialogSuccessLayoutBinding.inflate(layoutInflater))
                        .setAlertDialogText("Penjualan berhasil disimpan")
                        .setCancelable(true)
                        .setButtonText("Lihat detil")
                        .setButtonOnClickListener {
                            Log.d(LoginActivity.TAG, "fresh alert dialog success")
                            router.navigateToSaleDetailActivityAndFinish(
                                activity = this,
                                saleId = saleDetail.id
                            )
                        }
                        .create()
                        .show()
                }
                is ErrorResourceState -> {
                    viewBinding.pbSaleCreateSaleLoading.visibility = View.GONE
                    Log.e(TAG, state.error.toString())
                    router.navigateToSaleDetailActivityAndFinish(
                        activity = this,
                        saleId = saleDetail.id
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isSaleDone) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun onCreateSaleError(error: Exception) {
        Log.e(TAG, error.toString())
        viewBinding.pbSaleCreateSaleLoading.visibility = View.GONE
        when (error) {
            is IOException -> {

            }
            is HttpException -> {

            }
            else -> {

            }
        }
    }
}