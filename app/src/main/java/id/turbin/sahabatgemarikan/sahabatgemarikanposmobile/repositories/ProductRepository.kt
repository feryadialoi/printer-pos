package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.repositories

import android.util.Log
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis.ProductApi
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ProductUnit
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.GetProductsParam
import java.math.BigDecimal
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApi: ProductApi
) {
    suspend fun getProducts(getProductsParam: GetProductsParam): ApiResponse<List<Product>> {
        val token = "Bearer ${getProductsParam.token}"
        return productApi.getProducts(token = token)
    }
}