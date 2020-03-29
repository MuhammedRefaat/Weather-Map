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

        /**
         * Getting city weather condition by city name
         */
        fun getCitiesWeatherCondition(cityName: String) {
            val apiCaller = APIsController.adapter?.otherCitiesWeatherInfo(
                cityName,
                AppConstants.APP_KEY
            )
            if (apiCaller != null) {
                apiCaller.enqueue(object : Callback<APIsData> {
                    override fun onResponse(call: Call<APIsData>, response: Response<APIsData>) {
                        if (response.isSuccessful) {
                            try {
                                EventBus.getDefault().post(
                                    ServerResEvent(true, response.body(), null, cityName)
                                )
                            } catch (ex: Exception) {
                                EventBus.getDefault()
                                    .post(ServerResEvent(false, null, null, cityName))
                            }
                        } else {
                            EventBus.getDefault()
                                .post(ServerResEvent(false, null, response.errorBody(), cityName))
                        }
                    }

                    override fun onFailure(call: Call<APIsData>, t: Throwable) {
                        EventBus.getDefault().post(ServerResEvent(false, null, null, cityName))
                    }
                })
            } else {
                EventBus.getDefault().post(ServerResEvent(false, null, null, cityName))
            }
        }

        /**
         * Getting current city weather forecast by city location
         */
        fun getMyCityWeatherForecast(lat: String, lon: String) {
            val apiCaller =
                APIsController.adapter?.myCityWeatherForecast(lat, lon, AppConstants.APP_KEY)
            if (apiCaller != null) {
                apiCaller.enqueue(object : Callback<APIsData> {
                    override fun onResponse(call: Call<APIsData>, response: Response<APIsData>) {
                        if (response.isSuccessful) {
                            try {
                                EventBus.getDefault().post(
                                    ServerResEvent(
                                        true, response.body(),
                                        null, ""
                                    )
                                )
                            } catch (ex: Exception) {
                                EventBus.getDefault().post(
                                    ServerResEvent(
                                        false,
                                        null,
                                        null,
                                        ""
                                    )
                                )
                            }
                        } else {
                            EventBus.getDefault()
                                .post(ServerResEvent(false, null, response.errorBody(), ""))
                        }
                    }

                    override fun onFailure(call: Call<APIsData>, t: Throwable) {
                        EventBus.getDefault().post(ServerResEvent(false, null, null, ""))
                    }
                })
            } else {
                EventBus.getDefault().post(ServerResEvent(false, null, null, ""))
            }
        }

    }
}