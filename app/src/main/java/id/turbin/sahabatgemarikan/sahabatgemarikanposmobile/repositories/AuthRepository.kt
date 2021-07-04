package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.repositories

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis.AuthApi
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.LoginRequest
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.LoginResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Profile
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.ChangePasswordParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.ProfileParam
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepository @Inject constructor(
        private val authApi: AuthApi
) {
    suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        return authApi.login(loginRequest = loginRequest)
    }

    suspend fun changePassword(changePasswordParam: ChangePasswordParam): ApiResponse<Any> {
        val token = "Bearer ${changePasswordParam.token}"
        return authApi.changePassword(token = token, changePasswordRequest = changePasswordParam.changePasswordRequest)
    }


}