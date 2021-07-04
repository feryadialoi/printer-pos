package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls.FreshProductAdapterImpl
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.ProductAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FragmentProductBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.ProductViewModel
import retrofit2.HttpException
import javax.inject.Inject


@AndroidEntryPoint
class ProductFragment : Fragment() {

    @Inject
    lateinit var productViewModel: ProductViewModel

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authSharedPreferences: AuthSharedPreferences

    private lateinit var viewBinding: FragmentProductBinding
    private lateinit var productAdapter: ProductAdapter

    companion object {
        @JvmStatic
        private val TAG = "ProductFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProductBinding.inflate(inflater, container, false)


        initView()

        onLoadProducts(false)

        observeProducts()

        return viewBinding.root
    }

    private fun initView() {
        initSwipeToRefresh()
        initProductsRecyclerView()

        initSearch()
    }

    private fun initSearch() {
        viewBinding.etProductSearch
        viewBinding.etProductSearch.addTextChangedListener {
            onFilterProducts(keyword = it.toString())
        }

        viewBinding.etProductSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewBinding.etProductSearch.clearFocus()


                val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

            }


            true
        }
    }

    private fun onFilterProducts(keyword: String) {
        Log.d(TAG, keyword)
        productViewModel.filterProducts(keyword = keyword)
    }

    private fun initSwipeToRefresh() {
        viewBinding.srlProductRefresh.setOnRefreshListener {
            onSwipeToRefresh()
        }
    }

    private fun observeProducts() {
        productViewModel.products.observe(requireActivity()) { products ->
            productAdapterListen(products = products)
        }
    }


    private fun onLoadProducts(isRefreshing: Boolean = false) {
        productViewModel.loadProducts().observe(requireActivity()) { state ->
            when (state) {
                is LoadingResourceState -> {
                    onLoadProductsLoading()
                }
                is SuccessResourceState<*> -> {
                    onLoadProductsSuccess(isRefreshing = isRefreshing)
                }
                is ErrorResourceState -> {
                    onLoadProductsError(error = state.error, isRefreshing = isRefreshing)
                }
            }
        }
    }

    private fun onLoadProductsLoading() {
        viewBinding.pbProductLoadingProduct.visibility = View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    private fun onLoadProductsSuccess(isRefreshing: Boolean) {
        viewBinding.pbProductLoadingProduct.visibility = View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dismissRefreshing(isRefreshing = isRefreshing)
    }

    private fun onLoadProductsError(error: Exception, isRefreshing: Boolean = false) {
        viewBinding.pbProductLoadingProduct.visibility = View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        when (error) {
            is HttpException -> {
                Log.e(TAG, "HttpException: ${error.message()}")
            }
            else -> {
                Log.e(TAG, "else ${error.message}")
            }
        }

        dismissRefreshing(isRefreshing = isRefreshing)
    }


    private fun productAdapterListen(products: MutableList<Product>) {
        productAdapter.apply {
            this.addProducts(products = products)
            this.notifyDataSetChanged()
        }
    }

    private fun initProductsRecyclerView() {
        viewBinding.rvProductProduct.layoutManager = LinearLayoutManager(requireContext())
        productAdapter = FreshProductAdapterImpl(
            products = listOf(),
            onAdd = { product, index ->
                clearSearchFocusAndText()
                addProductToProductCart(product = product, index = index)
            },
            onDecrement = { product, index ->
                clearSearchFocusAndText()
                productViewModel.decrementProductQuantityOfProducts(
                    product = product,
                    index = index
                )
            },
            onIncrement = { product, index ->
                clearSearchFocusAndText()
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

    private fun clearSearchFocusAndText() {
        viewBinding.etProductSearch.text.clear()
        viewBinding.etProductSearch.clearFocus()
    }

    private fun addProductToProductCart(product: Product, index: Int) {
        productViewModel.incrementProductQuantityOfProducts(product = product, index = index)
    }

    private fun onSwipeToRefresh() {
//        onLoadProducts(isRefreshing = true)
        viewBinding.srlProductRefresh.isRefreshing = false
    }

    private fun dismissRefreshing(isRefreshing: Boolean) {
        if (isRefreshing) {
            viewBinding.srlProductRefresh.isRefreshing = false
        }
    }
}
