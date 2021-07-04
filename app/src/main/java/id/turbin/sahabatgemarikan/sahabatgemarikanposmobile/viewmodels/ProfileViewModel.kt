package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Profile
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.ProfileParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.repositories.ProfileRepository
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authSharedPreferences: AuthSharedPreferences
) {
    var profile = MutableLiveData<Profile>()

    fun loadProfiles(): LiveData<ResourceState> {
        return liveData(Dispatchers.IO) {
            emit(LoadingResourceState())
            try {
                val token = authSharedPreferences.getAuthToken()
                val profileParam = ProfileParam(token = token)
                val response = profileRepository.loadProfile(profileParam = profileParam)
                profile.postValue(response.data!!)
                emit(SuccessResourceState(data = response))
            } catch (e: Exception) {
                Log.e("profileViewModel", e.toString() )
                emit(ErrorResourceState(error = e))
            }
        }
    }
}