package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.GetProductsParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.repositories.ProductRepository
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@SuppressLint("StaticFieldLeak")
@Singleton
class ProductViewModel @Inject constructor(
        private val productRepository: ProductRepository,
        private val authSharedPreferences: AuthSharedPreferences
) : ViewModel() {

    var products = MutableLiveData<MutableList<Product>>()

    var masterProducts = MutableLiveData<MutableList<Product>>()

    fun filterProducts(keyword: String){
        var filteredProducts: MutableList<Product> = mutableListOf()
        if (keyword == "") {
           filteredProducts = masterProducts.value ?: mutableListOf()
        } else {
            filteredProducts = (masterProducts.value ?: mutableListOf()).filter { product -> product.name.contains(other = keyword, ignoreCase = true) } as MutableList<Product>
        }

        products.postValue(filteredProducts)
    }

    fun incrementProductQuantityOfProducts (product: Product, index: Int) {
//        val products = products.value!!
//        val indexOfProduct = products.indexOf(product)
//        products[indexOfProduct].quantity++
//
//        this.products.postValue(products)

        val masterProducts = masterProducts.value!!
        val indexOfMasterProduct = masterProducts.indexOf(product)
        masterProducts[indexOfMasterProduct].quantity++
        this.masterProducts.postValue(masterProducts)
        this.products.postValue(masterProducts)
    }

    fun decrementProductQuantityOfProducts (product: Product, index: Int) {
//        val products = products.value!!
//        val indexOfProduct = products.indexOf(product)
//        if (products[indexOfProduct].quantity > 0) {
//            products[indexOfProduct].quantity--
//            this.products.postValue(products)
//        }

        val masterProducts = products.value!!
        val indexOfMasterProduct = masterProducts.indexOf(product)
        if (masterProducts[indexOfMasterProduct].quantity > 0) {
            masterProducts[indexOfMasterProduct].quantity--
            this.masterProducts.postValue(masterProducts)
            this.products.postValue(masterProducts)
        }
    }

    fun onEditProductQuantityOfProducts(index: Int, quantity: Int) {
        products.value!![index].quantity = quantity
        products.postValue(products.value)
    }

    fun loadProducts(): LiveData<ResourceState> {
        return liveData(Dispatchers.IO) {
            emit(LoadingResourceState())
            try {
                val token = authSharedPreferences.getAuthToken()
                val getProductsParam = GetProductsParam(token = token)
                val response = productRepository.getProducts(getProductsParam = getProductsParam)
                products.postValue(response.data as MutableList<Product>)
                masterProducts.postValue(response.data as MutableList<Product>)
                emit(SuccessResourceState(data = response))
            } catch (e: Exception) {
                emit(ErrorResourceState(error = e))
            }
        }
    }
}