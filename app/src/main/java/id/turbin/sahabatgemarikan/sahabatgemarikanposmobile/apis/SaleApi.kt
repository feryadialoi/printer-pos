package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.CreateSaleRequest
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Sale
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleDetail
import retrofit2.Call
import retrofit2.http.*

interface SaleApi {
    @Headers(value = ["Accept: application/json"])
    @GET(value = "/api/v1/sales")
    suspend fun getSales(@Header(value = "Authorization") token: String, @Query(value = "date") date: String?): ApiResponse<List<Sale>>

    @Headers(value = ["Accept: application/json"])
    @GET(value = "/api/v1/sales/{saleId}")
    suspend fun getSaleDetail(@Header(value = "Authorization") token: String, @Path(value = "saleId") saleId: Long): ApiResponse<SaleDetail>

    @Headers(value = ["Accept: application/json"])
    @POST(value = "/api/v1/sales")
    suspend fun createSale(@Header(value = "Authorization") token: String, @Body createSaleRequest: CreateSaleRequest): ApiResponse<SaleDetail>

}