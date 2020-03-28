package com.imagine.weathermap.network

import com.imagine.weathermap.controllers.APIsController
import com.imagine.weathermap.misc.AppConstants
import com.imagine.weathermap.models.APIsData
import com.imagine.weathermap.models.ServerResEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIsCaller {

    companion object {

        fun getCitiesWeatherCondition(citiesCommaSeparated: String) {
            val apiCaller = APIsController.adapter?.otherCitiesWeatherInfo(
                citiesCommaSeparated,
                AppConstants.APP_KEY
            )
            if (apiCaller != null) {
                apiCaller.enqueue(object : Callback<APIsData> {
                    override fun onResponse(call: Call<APIsData>, response: Response<APIsData>) {
                        if (response.isSuccessful) {
                            try {
                                EventBus.getDefault().post(ServerResEvent(true, response.body()))
                            } catch (ex: Exception) {
                                EventBus.getDefault().post(ServerResEvent(false, null))
                            }
                        } else {
                            EventBus.getDefault().post(ServerResEvent(false, null))
                        }
                    }

                    override fun onFailure(call: Call<APIsData>, t: Throwable) {
                        EventBus.getDefault().post(ServerResEvent(false, null))
                    }
                })
            } else {
                EventBus.getDefault().post(ServerResEvent(false, null))
            }
        }

        fun getMyCityWeatherForecast(lat: String, lon: String) {
            val apiCaller =
                APIsController.adapter?.myCityWeatherForecast(lat, lon, AppConstants.APP_KEY)
            if (apiCaller != null) {
                apiCaller.enqueue(object : Callback<APIsData> {
                    override fun onResponse(call: Call<APIsData>, response: Response<APIsData>) {
                        if (response.isSuccessful) {
                            try {
                                EventBus.getDefault().post(ServerResEvent(true, response.body()))
                            } catch (ex: Exception) {
                                EventBus.getDefault().post(ServerResEvent(false, null))
                            }
                        } else {
                            EventBus.getDefault().post(ServerResEvent(false, null))
                        }
                    }

                    override fun onFailure(call: Call<APIsData>, t: Throwable) {
                        EventBus.getDefault().post(ServerResEvent(false, null))
                    }
                })
            } else {
                EventBus.getDefault().post(ServerResEvent(false, null))
            }
        }

    }
}