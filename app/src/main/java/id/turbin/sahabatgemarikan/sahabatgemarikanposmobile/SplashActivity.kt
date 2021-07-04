package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    @Inject
    lateinit var authSharedPreferences: AuthSharedPreferences

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Log.d(TAG, "onCreate")

        GlobalScope.launch {
            splashOff()
        }
    }

    private suspend fun splashOff() {
        delay(2000L)

        if (authSharedPreferences.getAuthToken().isEmpty()) {
            router.navigateToLoginActivityAndFinish(this)
        } else {
            router.navigateToMainActivityAndFinish(this)
        }
    }
}