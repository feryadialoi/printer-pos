package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences

import android.content.Context
import android.icu.lang.UScript.getCode
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.User
import javax.inject.Inject

class UserSharedPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getProfile(): User {
        return User(
            code = getCode(),
            name = getUserName(),
            email = getUserEmail(),
            phone = getUserPhone(),
            address = getUserAddress(),
        )
    }

    fun setProfile(user: User) {
        setCode(user.code)
        setUserName(user.name)
        setUserEmail(user.email)
        setUserPhone(user.phone)
        setUserAddress(user.address)
    }

    fun removeProfile() {
        removeCode()
        removeUserName()
        removeUserEmail()
        removeUserPhone()
        removeUserAddress()
    }

    private fun getCode(): String{
        return preferences.getString("code", "")!!
    }

    private fun setCode(code: String){
        return preferences.edit().putString("code", code).apply()
    }

    private fun getUserName(): String {
        return preferences.getString("name", "")!!
    }

    private fun setUserName(name: String) {
        preferences.edit().putString("name", name).apply()
    }

    private fun getUserEmail(): String {
        return preferences.getString("email", "")!!
    }

    private fun setUserEmail(email: String) {
        preferences.edit().putString("email", email).apply()
    }

    private fun getUserPhone(): String {
        return preferences.getString("phone", "")!!
    }

    private fun setUserPhone(phone: String) {
        preferences.edit().putString("phone", phone).apply()
    }

    private fun getUserAddress(): String {
        return preferences.getString("address", "")!!
    }

    private fun setUserAddress(address: String) {
        preferences.edit().putString("address", address).apply()
    }

    private fun removeCode() {
        preferences.edit().remove("code").apply()
    }

    private fun removeUserName() {
        preferences.edit().remove("name").apply()
    }

    private fun removeUserEmail() {
        preferences.edit().remove("email").apply()
    }

    private fun removeUserPhone() {
        preferences.edit().remove("phone").apply()
    }

    private fun removeUserAddress() {
        preferences.edit().remove("address").apply()
    }
}