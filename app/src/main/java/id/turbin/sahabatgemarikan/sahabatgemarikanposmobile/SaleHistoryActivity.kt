package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapterimpls.FreshSaleAdapterImpl
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.adapters.SaleAdapter
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivitySaleHistoryBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Sale
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleDate
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils.DateUtil
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.SaleViewModel
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SaleHistoryActivity : BaseActivity() {

    @Inject
    lateinit var saleViewModel: SaleViewModel

    @Inject
    lateinit var router: Router

    private lateinit var viewBinding: ActivitySaleHistoryBinding

    private lateinit var saleAdapter: SaleAdapter

    private val calendar = Calendar.getInstance()

    companion object {
        @JvmStatic
        private val TAG = "SaleHistoryActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySaleHistoryBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        Log.d(TAG, "onCreate")

        initView()

        setDate()

        loadSales(date = DateUtil.serverDateFormat(calendar))

        observeSales()

        observeDate()

    }

    private fun initView() {
        initToolbar()
        initSalesRecyclerView()
        initSwipeToRefresh()
        initSelectDate()
        initNoContentView()
    }

    private fun initNoContentView() {
        viewBinding.btnFreshSaleHistoryBackToProduct.tvFreshButtonSmallText.text = "Buat penjualan"
        viewBinding.btnFreshSaleHistoryBackToProduct.cvFreshButtonSmall.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getSaleDate(): SaleDate {
        val saleDate = saleViewModel.saleDate.value
        if (saleDate != null) {
            return saleDate
        } else {
            return SaleDate(
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                month = calendar.get(Calendar.MONTH),
                year = calendar.get(Calendar.YEAR)
            )
        }
    }

    private fun initSelectDate() {

        val saleDate = getSaleDate()

        viewBinding.tvSaleHistorySelectDate.text = DateUtil.localDateFormat(
            dayOfMonth = saleDate.dayOfMonth,
            month = saleDate.month,
            year = saleDate.year
        )

        viewBinding.cvSaleHistorySelectDate.setOnClickListener {
            onClickSelectDate()
        }
    }

    private fun initSwipeToRefresh() {
        viewBinding.srlSaleHistoryRefresh.setOnRefreshListener {
            onSwipeToRefresh()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.tbActivitySaleHistory)
        supportActionBar?.apply {
            title = getString(R.string.sale_history_activity_title)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun initSalesRecyclerView() {
        viewBinding.rvSaleHistorySale.layoutManager = LinearLayoutManager(this)
        saleAdapter = FreshSaleAdapterImpl(
            sales = mutableListOf(),
            onClick = { sale, index ->
                onClickSaleItem(saleId = sale.id)
            }
        )
        viewBinding.rvSaleHistorySale.adapter = saleAdapter
    }

    private fun onClickSelectDate() {
        val saleDate = getSaleDate()
        DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->

                saleViewModel.setDate(
                    DateUtil.serverDateFormat(
                        dayOfMonth = dayOfMonth,
                        month = month,
                        year = year
                    )
                )
                saleViewModel.setSaleDate(
                    SaleDate(
                        dayOfMonth = dayOfMonth,
                        month = month,
                        year = year
                    )
                )
                viewBinding.tvSaleHistorySelectDate.text =
                    DateUtil.localDateFormat(dayOfMonth, month, year)

            },
            saleDate.year,
            saleDate.month,
            saleDate.dayOfMonth,
        ).show()

    }

    private fun onClickSaleItem(saleId: Long) {
        router.navigateToSaleDetailActivity(activity = this, saleId = saleId)
    }

    private fun setDate() {
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        saleViewModel.setSaleDate(
            saleDate = SaleDate(
                dayOfMonth = dayOfMonth,
                month = month,
                year = year,
            )
        )

        saleViewModel.setDate(
            DateUtil.serverDateFormat(
                dayOfMonth = dayOfMonth,
                month = month,
                year = year,
            )
        )
    }

    private fun observeDate() {
        saleViewModel.date.observe(this) { date ->
            loadSales(date = date)
        }
    }

    private fun observeSales() {
        saleViewModel.sales.observe(this) { sales ->
            saleAdapterListener(sales = sales)
            toggleNoContentListener(sales = sales)
        }
    }

    private fun toggleNoContentListener(sales: MutableList<Sale>) {
        if (sales.isEmpty()) {
            viewBinding.llSaleHistoryNoContent.visibility = View.VISIBLE
        } else {
            viewBinding.llSaleHistoryNoContent.visibility = View.GONE
        }
    }

    private fun saleAdapterListener(sales: MutableList<Sale>) {
        saleAdapter.apply {
            sales.reverse()
            this.addSales(sales = sales)
            this.notifyDataSetChanged()
        }
    }

    private fun loadSales(date: String? = null, isRefreshing: Boolean = false) {
        Log.d(TAG, "loadSale, date: $date")
        saleViewModel.loadSales(date = date)
            .observe(this) { state ->
                when (state) {
                    is LoadingResourceState -> {
                        onLoadSalesLoading()
                    }
                    is SuccessResourceState<*> -> {
                        onLoadSalesSuccess(isRefreshing = isRefreshing)
                    }
                    is ErrorResourceState -> {
                        onLoadSalesError(error = state.error, isRefreshing = isRefreshing)
                    }
                }
            }
    }

    private fun onLoadSalesLoading() {
        viewBinding.pbSaleHistorySalesLoading.visibility = View.VISIBLE
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    private fun onLoadSalesSuccess(isRefreshing: Boolean) {
        dismissRefreshing(isRefreshing)
        viewBinding.pbSaleHistorySalesLoading.visibility = View.GONE
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private fun onLoadSalesError(error: Exception, isRefreshing: Boolean) {
        dismissRefreshing(isRefreshing)
        viewBinding.pbSaleHistorySalesLoading.visibility = View.GONE
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        when (error) {
            is IOException -> {
                Log.e(TAG, "IOException: $error")
            }
            is HttpException -> {
                Log.e(TAG, "HttpException: $error")
            }
            else -> {
                Log.e(TAG, "else: $error")
            }
        }
    }

    private fun onSwipeToRefresh() {
        val date = saleViewModel.date.value
        loadSales(date = date, isRefreshing = true)
//        viewBinding.srlSaleHistoryRefresh.isRefreshing = false
    }

    private fun dismissRefreshing(isRefreshing: Boolean) {
        if (isRefreshing) {
            viewBinding.srlSaleHistoryRefresh.isRefreshing = false
        }
    }
}