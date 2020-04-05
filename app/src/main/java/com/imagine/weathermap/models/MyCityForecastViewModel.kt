package com.imagine.weathermap.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CityForecastViewModel : ViewModel(){

    val weatherForecastData  = MutableLiveData<APIsData>()

    val weatherForecastDataError  = MutableLiveData<Boolean>()

    val loading  = MutableLiveData<Boolean>()

    fun refresh(){
        getMyCityWeatherForecast()
    }

    private fun getMyCityWeatherForecast(){

    }

}