package com.imagine.weathermap.controllers

import android.content.Context
import com.dinuscxj.progressbar.CircleProgressBar
import com.imagine.weathermap.network.APIsCaller
import com.imagine.weathermap.network.APIsInterface
import com.imagine.weathermap.network.APIsclient

class APIsController {

    companion object {
        var adapter: APIsInterface? = null
        var appContext: Context? = null

        private var st: APIsController? = null

        var progressIndicator: CircleProgressBar? = null

        fun initAPIsController() {
            adapter = APIsclient.adapter!!
        }

        fun getInstance(appContext: Context): APIsController {
            APIsController.appContext = appContext
            if (st == null) {
                st = APIsController()
                initAPIsController()
                adapter = APIsclient.adapter
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