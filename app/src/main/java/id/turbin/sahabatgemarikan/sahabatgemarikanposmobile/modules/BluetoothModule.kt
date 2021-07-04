package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.modules

import android.bluetooth.BluetoothAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object BluetoothModule {
    @Singleton
    @Provides
    fun provideBluetoothAdapter(): BluetoothAdapter {
        return BluetoothAdapter.getDefaultAdapter()
    }
}