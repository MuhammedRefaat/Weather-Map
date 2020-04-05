package com.imagine.weathermap.models

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagine.weathermap.network.APIsCaller
import com.imagine.weathermap.network.APIsClient
import com.imagine.weathermap.network.DaggerApiComponent
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CitySearchWeatherViewModel : ViewModel() {

    @Inject
    lateinit var weatherService: APIsCaller

    private lateinit var context: Context

    val weatherDetailsList = HashMap<String, APIsData>()

    val weatherDetailsListError = HashMap<String, ResponseBody?>()

    val weatherDetailsData = MutableLiveData<Map<String, APIsData>>()

    val weatherDetailsDataError = MutableLiveData<Map<String, ResponseBody?>>()

    val loading = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.builder()
            .aPIsClient(APIsClient)
            .build().inject(this)
    }

    fun getWeatherDetails(context: Context, cities: List<String>) {
        this.context = context
        getCitiesWeatherData(cities)
    }

    private fun getCitiesWeatherData(cities: List<String>) {
        for (city in cities) {
            weatherService.getCityWeatherCondition(city).enqueue(object : Callback<APIsData> {
                override fun onResponse(call: Call<APIsData>, response: Response<APIsData>) {
                    loading.value = false
                    if (response.isSuccessful) {
                        try {
                            weatherDetailsList[city] = response.body()!!
                            weatherDetailsData.value = weatherDetailsList
                        } catch (ex: Exception) {
                            weatherDetailsListError[city] = null
                            weatherDetailsDataError.value = weatherDetailsListError
                        }
                    } else {
                        weatherDetailsListError[city] = response.errorBody()!!
                        weatherDetailsDataError.value = weatherDetailsListError
                    }
                }

                override fun onFailure(call: Call<APIsData>, t: Throwable) {
                    loading.value = false
                    weatherDetailsListError[city] = null
                    weatherDetailsDataError.value = weatherDetailsListError
                }
            })
        }
    }

}