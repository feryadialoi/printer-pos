package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.*
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.CreateSaleParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.GetSaleDetailParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.GetSalesParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.repositories.SaleRepository
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class SaleViewModel @Inject constructor(
        private val saleRepository: SaleRepository,
        private val authSharedPreferences: AuthSharedPreferences,
) : ViewModel() {
    var sales = MutableLiveData<MutableList<Sale>>()
    var saleDetail = MutableLiveData<SaleDetail>()
    var date = MutableLiveData<String>()
    var saleDate = MutableLiveData<SaleDate>()

    fun loadSales(date: String? = null): LiveData<ResourceState> {
        return liveData(Dispatchers.IO) {
            emit(LoadingResourceState())
            try {
                val token = authSharedPreferences.getAuthToken()
                val getSalesParam = GetSalesParam(token = token, date = date)
                val response = saleRepository.getSales(getSalesParam = getSalesParam)
                sales.postValue(response.data as MutableList<Sale>)
                emit(SuccessResourceState(data = response))
            } catch (e: Exception) {
                emit(ErrorResourceState(error = e))
            }
        }
    }

    fun loadSaleDetail(saleId: Long): LiveData<ResourceState> {
        return liveData(Dispatchers.IO) {
            emit(LoadingResourceState())
            try {
                val token = authSharedPreferences.getAuthToken()
                val getSaleDetailParam = GetSaleDetailParam(token = token, saleId = saleId)
                val response = saleRepository.getSaleDetail(getSaleDetailParam = getSaleDetailParam)
                saleDetail.postValue(response.data!!)
                emit(SuccessResourceState(data = response))
            } catch (e: Exception) {
                Log.d("sale view model", e.toString())
                emit(ErrorResourceState(error = e))
            }
        }
    }

    fun createSale(createSaleRequest: CreateSaleRequest): LiveData<ResourceState> {
        return liveData(Dispatchers.IO) {
            emit(LoadingResourceState())
            try {
                val token = authSharedPreferences.getAuthToken()
                val createSaleParam = CreateSaleParam(token = token, createSaleRequest = createSaleRequest)
                val response: ApiResponse<SaleDetail> = saleRepository.createSale(createSaleParam = createSaleParam)
                emit(SuccessResourceState(data = response))
            } catch (e: Exception) {
                emit(ErrorResourceState(error = e))
            }
        }
    }

    fun setDate(date: String) {
        this.date.postValue(date)
    }

    fun setSaleDate(saleDate: SaleDate) {
        this.saleDate.postValue(saleDate)
    }
}