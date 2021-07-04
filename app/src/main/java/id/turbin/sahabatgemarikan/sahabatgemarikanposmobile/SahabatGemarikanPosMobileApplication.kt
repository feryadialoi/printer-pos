package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.app.Application
import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SahabatGemarikanPosMobileApplication : Application() {
    lateinit var bluetoothSocket: BluetoothSocket

    fun getIsBluetoothSocketInitialized() = this::bluetoothSocket.isInitialized

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}