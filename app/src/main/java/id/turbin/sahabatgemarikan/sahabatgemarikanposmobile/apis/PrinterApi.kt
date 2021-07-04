package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PrinterApi {
    @Headers(value = ["Accept: application/json"])
    @POST(value = "/api/v1/printers")
    suspend fun createPrinter(@Header(value = "Authorization") token: String): ApiResponse<Any>
}