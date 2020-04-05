package com.imagine.weathermap.network

import com.imagine.weathermap.misc.AppConstants
import com.imagine.weathermap.models.APIsData
import retrofit2.Call
import javax.inject.Inject

class APIsCaller {

    @Inject
    lateinit var api: APIsInterface

    init {
        DaggerApiComponent.builder()
            .aPIsClient(APIsClient)
            .build().inject(this)
    }

    /**
     * Getting current city weather forecast by city location
     */
    fun getMyCityWeatherForecast(lat: String, lon: String): Call<APIsData> {
        return api.myCityWeatherForecast(lat, lon, AppConstants.APP_KEY)
    }

    /**
     * Getting city weather condition by city name
     */
    fun getCityWeatherCondition(cityName: String): Call<APIsData> {
        return api.otherCitiesWeatherInfo(cityName, AppConstants.APP_KEY)
    }

}