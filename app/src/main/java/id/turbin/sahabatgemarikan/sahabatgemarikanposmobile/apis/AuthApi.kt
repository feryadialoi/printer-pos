package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ChangePasswordRequest
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.LoginResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.LoginRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @Headers(value = ["Accept: application/json"])
    @POST(value = "/api/v1/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>

    @Headers(value = ["Accept: application/json"])
    @POST(value = "/api/v1/change-password")
    suspend fun changePassword(
            @Header(value = "Authorization") token: String,
            @Body changePasswordRequest: ChangePasswordRequest): ApiResponse<Any>
}