package com.imagine.weathermap.network

import com.imagine.weathermap.misc.AppConstants
import com.imagine.weathermap.models.APIsData
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Muhammed Refaat on 28/3/2020.
 */

interface APIsInterface {

    @POST(AppConstants.MY_CITY_API_URL_EXT)
    fun myCityWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String
    ): Single<APIsData>

    @POST(AppConstants.OTHER_CITIES_API_URL_EXT)
    fun otherCitiesWeatherInfo(
        @Query("q") cityName: String,
        @Query("appid") appId: String
    ): Single<APIsData>

}