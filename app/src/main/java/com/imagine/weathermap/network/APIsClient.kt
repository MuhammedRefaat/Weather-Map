package com.imagine.weathermap.network

import com.imagine.weathermap.BuildConfig
import com.imagine.weathermap.misc.AppConstants
import dagger.Module
import dagger.Provides

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Muhammed Refaat on 28/3/2020.
 */
@Module
object APIsClient {

    private const val CONNECT_TIMEOUT = 10 // in secs
    private const val READ_WRITE_TIMEOUT = 100 // in secs

    @Provides
    fun initClient(): APIsInterface {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(READ_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout(READ_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.addInterceptor(loggingInterceptor)
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .baseUrl(AppConstants.APIS_URL).build()
            .create(APIsInterface::class.java)
    }

    @Provides
    fun provideAPIsCallService(): APIsCaller {
        return APIsCaller()
    }

}
