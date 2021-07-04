package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Profile
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ProfileApi {
    @Headers(value = ["Accept: application/json"])
    @GET(value = "/api/v1/profiles")
    suspend fun getProfile(@Header(value = "Authorization") token: String): ApiResponse<Profile>
}