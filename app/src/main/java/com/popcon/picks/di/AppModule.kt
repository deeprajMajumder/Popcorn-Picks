package com.popcon.picks.di

import android.content.Context
import android.content.SharedPreferences
import com.popcon.picks.dataSource.localDataBase.AppDataBase
import com.popcon.picks.dataSource.network.NetworkingService
import com.popcon.picks.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * App scoped module for dependency injections
 * Using it to allow for better testability of the app since Hilt is so fast to stand up
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideRetrofit(): NetworkingService =
        Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(NetworkingService::class.java)

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        return client.build()
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDataBase {
        return AppDataBase(appContext)
    }
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("PopcornPicks", Context.MODE_PRIVATE)
    }
}