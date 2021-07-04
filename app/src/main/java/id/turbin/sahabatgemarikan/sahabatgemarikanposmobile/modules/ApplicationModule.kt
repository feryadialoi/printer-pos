package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.Validator
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideRouter(): Router {
        return Router()
    }

    @Singleton
    @Provides
    fun provideValidator(): Validator {
        return Validator()
    }
}