package org.openlake.sampoorna.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.openlake.sampoorna.data.auth.AuthApi
import org.openlake.sampoorna.data.repository.AuthRepository
import org.openlake.sampoorna.data.repository.AuthRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthApi(app : Application) : AuthApi {
        val cacheSize = (10*1024*1024).toLong()
        val cache = Cache(app.cacheDir, cacheSize)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor{
                var request = it.request()
                request = if (hasNetwork(app) ==true)
                    request.newBuilder().header("Cache-Control","public, max-age="+5).build()
                else
                    request.newBuilder().header("Cache-Control","public, only-if-cached, max-stale="+60*60*24*7).build()
                it.proceed(request)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl("https://sampoorna-backend.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app : Application) : SharedPreferences {
        return app.getSharedPreferences("jwt",Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api : AuthApi,preferences: SharedPreferences) : AuthRepository {
        return AuthRepositoryImpl(api,preferences)
    }

    private fun hasNetwork(app : Application): Boolean? {
        var isConnected: Boolean? = false
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}