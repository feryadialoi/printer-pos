package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ProductApi {
    @Headers(value = ["Accept: application/json"])
    @GET(value = "/api/v1/products")
    suspend fun getProducts(@Header(value = "Authorization") token: String): ApiResponse<List<Product>>
}