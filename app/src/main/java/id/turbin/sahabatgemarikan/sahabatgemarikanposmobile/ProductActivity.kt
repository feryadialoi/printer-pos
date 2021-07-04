package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls.ProductAdapterImpl
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivityProductBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.ProductViewModel
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class ProductActivity : BaseActivity() {

    private val TAG = this::class.simpleName

    @Inject
    lateinit var productViewModel: ProductViewModel

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authSharedPreferences: AuthSharedPreferences

    private lateinit var viewBinding: ActivityProductBinding
    private lateinit var productAdapter: ProductAdapterImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProductBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_product)
        setContentView(viewBinding.root)

        Log.d(TAG, "onCreate")

        Log.d(TAG, authSharedPreferences.getAuthToken())


        initView()

        onLoadProducts()

        observeProducts()


        viewBinding.btnGoToSaleHistory.setOnClickListener {
            router.navigateToSaleHistoryActivity(this)
        }
        viewBinding.btnGoToSaleDetail.setOnClickListener {
            router.navigateToSaleDetailActivity(this, 1)
        }
        viewBinding.btnGoToSetting.setOnClickListener {
            router.navigateToSettingActivity(this)
        }
    }

    private fun onLoadProducts() {
        productViewModel.loadProducts().observe(this) { state ->
            when (state) {
                is LoadingResourceState -> {
                    viewBinding.pbProductLoadingProduct.visibility = View.VISIBLE
                }
                is SuccessResourceState<*> -> {
                    viewBinding.pbProductLoadingProduct.visibility = View.GONE
                }
                is ErrorResourceState -> {
                    viewBinding.pbProductLoadingProduct.visibility = View.GONE
                    onLoadProductsError(error = state.error)
                }
            }
        }
    }

    private fun observeProducts() {
        productViewModel.products.observe(this) { products ->

            productsQuantityListen(products = products)

            productAdapterListen(products = products)
        }
    }

    private fun productAdapterListen(products: MutableList<Product>) {
        productAdapter.apply {
            this.addProducts(products = products)
            this.notifyDataSetChanged()
        }
    }

    private fun productsQuantityListen(products: MutableList<Product>) {
        val productsWithQuantityNotZero = products.filter { product -> product.quantity > 0 }
        if (productsWithQuantityNotZero.isEmpty()) {
            viewBinding.tvProductCartCount.visibility = View.INVISIBLE
        } else {
            viewBinding.tvProductCartCount.visibility = View.VISIBLE
        }
        viewBinding.tvProductCartCount.text =
            products.filter { product -> product.quantity > 0 }.size.toString()
    }

    private fun initView() {
        initSwipeToRefresh()
        initProductsRecyclerView()
        initToolbar()
    }

    private fun initSwipeToRefresh() {
        viewBinding.srlProductRefresh.setOnRefreshListener {
            onSwipeToRefresh()
        }
    }

    private fun onLoadProductsError(error: Exception) {
        when (error) {
            is HttpException -> {
                Log.e(TAG, "HttpException: ${error.message()}")
            }
            else -> {
                Log.e(TAG, "else ${error.message}")
            }
        }
    }

    private fun addProductToProductCart(product: Product, index: Int) {
        productViewModel.incrementProductQuantityOfProducts(product = product, index = index)
    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.tbActivityProduct)
        supportActionBar?.apply {
            title = "Sahabat Gemarikan"
            elevation = 15F

            viewBinding.ibProductCart.setOnClickListener {
                goToSaleActivity()
            }
        }


    }


    private fun goToSaleActivity() {
        router.navigateToSaleActivity(this)
    }

    private fun initProductsRecyclerView() {
        viewBinding.rvProductProduct.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapterImpl(
            products = listOf(),
            onAdd = { product, index ->
                addProductToProductCart(product = product, index = index)
            },
            onDecrement = { product, index ->
                productViewModel.decrementProductQuantityOfProducts(
                    product = product,
                    index = index
                )
            },
            onIncrement = { product, index ->
                productViewModel.incrementProductQuantityOfProducts(
                    product = product,
                    index = index
                )
            },
            onEditText = { index, quantity ->
                productViewModel.onEditProductQuantityOfProducts(index = index, quantity = quantity)
            }
        )
        viewBinding.rvProductProduct.adapter = productAdapter
    }


    private fun onSwipeToRefresh() {
        viewBinding.srlProductRefresh.isRefreshing = false
    }


}