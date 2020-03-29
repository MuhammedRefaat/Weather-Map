package com.imagine.weathermap.network

import com.imagine.weathermap.BuildConfig
import com.imagine.weathermap.misc.AppConstants

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Muhammed Refaat on 28/3/2020.
 */

internal object APIsClient {

    var adapter: APIsInterface? = null
    private const val CONNECT_TIMEOUT = 10 // in secs
    private const val READ_WRITE_TIMEOUT = 100 // in secs

    private var retrofit: Retrofit? = null

    init {
        initClient()
    }

    private fun initClient() {

        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(READ_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout(READ_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.addInterceptor(loggingInterceptor)

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .baseUrl(AppConstants.APIS_URL).build()
        }
        adapter = retrofit!!.create(APIsInterface::class.java)
    }

}
