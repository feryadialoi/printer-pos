package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis.AuthApi
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ChangePasswordRequest
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.LoginRequest
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.LoginResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.ChangePasswordParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.repositories.AuthRepository
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authSharedPreferences: AuthSharedPreferences
) : ViewModel() {

    fun login(loginRequest: LoginRequest): LiveData<ResourceState> {
        return liveData(Dispatchers.IO) {
            emit(LoadingResourceState())
            try {
                val response: ApiResponse<LoginResponse> =
                    authRepository.login(loginRequest = loginRequest)
                emit(SuccessResourceState(data = response))
            } catch (e: Exception) {
                emit(ErrorResourceState(error = e))
            }
        }
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest): LiveData<ResourceState> {
        return liveData(Dispatchers.IO) {
            emit(LoadingResourceState())
            try {
                val token = authSharedPreferences.getAuthToken()
                val changePasswordParam = ChangePasswordParam(
                    token = token,
                    changePasswordRequest = changePasswordRequest
                )
                val response: ApiResponse<*> =
                    authRepository.changePassword(changePasswordParam = changePasswordParam)
                emit(SuccessResourceState(data = response))
            } catch (e: Exception) {
                Log.e("auth view model", e.toString())
                emit(ErrorResourceState(error = e))
            }
        }
    }

}