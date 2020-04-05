package com.imagine.weathermap.models

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagine.weathermap.controllers.APIsController

class MyCityForecastViewModel : ViewModel() {

    private lateinit var context: Context

    private lateinit var latitude: String

    private lateinit var longitude: String

    val weatherForecastData = MutableLiveData<APIsData>()

    val weatherForecastDataError = MutableLiveData<Boolean>()

    val loading = MutableLiveData<Boolean>()


    fun getWeatherForecast(context: Context, lat: String, lon: String) {
        this.context = context
        this.latitude = lat
        this.longitude = lon
        getMyCityWeatherForecast()
    }

    private fun getMyCityWeatherForecast() {
        APIsController.getInstance(context)
            .getMyCityWeatherForecast(latitude, longitude)
    }

}