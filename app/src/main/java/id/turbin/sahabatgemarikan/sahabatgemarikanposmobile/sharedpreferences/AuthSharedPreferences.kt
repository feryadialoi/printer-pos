package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences

import android.content.Context
import androidx.preference.PreferenceManager
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
class AuthSharedPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val TOKEN = "token"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getAuthToken(): String {
        return preferences.getString(TOKEN, "")!!
    }

    fun setAuthToken(token: String) {
        preferences.edit().putString(TOKEN, token).apply()
    }

    fun removeAuthToken() {
        preferences.edit().remove(TOKEN).apply()
    }

}