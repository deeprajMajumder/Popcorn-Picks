package com.popcon.picks.di

import com.popcon.picks.dataSource.network.NetworkingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

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
            .baseUrl(BASE_URL)
            .build()
            .create(NetworkingService::class.java)

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        return client.build()
    }
    companion object {
        const val NETWORK_REQUEST_TIMEOUT_SECONDS = 30L
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/"
    }
}