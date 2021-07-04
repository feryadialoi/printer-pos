package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.modules

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis.*
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.interceptors.UnauthorizedInterceptor
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.properties.Environment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(UnauthorizedInterceptor())
            .build()

        val gson = GsonBuilder()
                .setLenient()
                .create()

        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Environment.BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideProductApi(retrofit: Retrofit): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSaleApi(retrofit: Retrofit): SaleApi {
        return retrofit.create(SaleApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun providePrinterApi(retrofit: Retrofit): PrinterApi {
        return retrofit.create(PrinterApi::class.java)
    }
}