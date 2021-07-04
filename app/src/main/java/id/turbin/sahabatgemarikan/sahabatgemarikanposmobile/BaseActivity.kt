package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.eventmodels.UnauthorizedEvent
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.UserSharedPreferences
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


open class BaseActivity: AppCompatActivity() {


    private lateinit var authSharedPreferences: AuthSharedPreferences

    private lateinit var userSharedPreferences: UserSharedPreferences


    override fun onStart() {

        authSharedPreferences = AuthSharedPreferences(this)
        userSharedPreferences = UserSharedPreferences(this)


        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onUnauthorizedEvent(e: UnauthorizedEvent) {
        handleUnauthorizedEvent()
    }

    protected open fun handleUnauthorizedEvent() {

        logout()
    }

    protected fun logout() {
        authSharedPreferences.removeAuthToken()
        userSharedPreferences.removeProfile()


        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}