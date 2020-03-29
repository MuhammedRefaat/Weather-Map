package com.imagine.weathermap.controllers

import android.content.Context
import com.imagine.weathermap.network.APIsCaller
import com.imagine.weathermap.network.APIsInterface
import com.imagine.weathermap.network.APIsClient

class APIsController {

    companion object {
        var adapter: APIsInterface? = null
        var appContext: Context? = null

        private var st: APIsController? = null

        private fun initAPIsController() {
            adapter = APIsClient.adapter!!
        }

        fun getInstance(appContext: Context): APIsController {
            APIsController.appContext = appContext
            if (st == null) {
                st = APIsController()
                initAPIsController()
                adapter = APIsClient.adapter
            }
            return st as APIsController
        }

    }

    fun getCitiesWeatherCondition(cityName: String) {
        APIsCaller.getCitiesWeatherCondition(cityName)
    }

    fun getMyCityWeatherForecast(lat: String, lon: String) {
        APIsCaller.getMyCityWeatherForecast(lat, lon)
    }

}