package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.repositories

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis.ProfileApi
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Profile
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.ProfileParam
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val profileApi: ProfileApi) {
    suspend fun loadProfile(profileParam: ProfileParam): ApiResponse<Profile> {
        val token = "Bearer ${profileParam.token}"
        return profileApi.getProfile(token = token)
    }
}