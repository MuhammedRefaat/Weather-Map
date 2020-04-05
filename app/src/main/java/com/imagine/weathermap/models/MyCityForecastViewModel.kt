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

class MyCityForecastViewModel : ViewModel() {

    @Inject
    lateinit var weatherService: APIsCaller

    private lateinit var context: Context

    private lateinit var latitude: String

    private lateinit var longitude: String

    val weatherForecastData = MutableLiveData<APIsData>()

    val weatherForecastDataError = MutableLiveData<ResponseBody>()

    val loading = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.builder()
            .aPIsClient(APIsClient)
            .build().inject(this)
    }

    fun getWeatherForecast(context: Context, lat: String, lon: String) {
        this.context = context
        this.latitude = lat
        this.longitude = lon
        getMyCityWeatherForecast()
    }

    private fun getMyCityWeatherForecast() {
        weatherService.getMyCityWeatherForecast(latitude, longitude).enqueue(object :
            Callback<APIsData> {
            override fun onResponse(call: Call<APIsData>, response: Response<APIsData>) {
                loading.value = false
                if (response.isSuccessful) {
                    try {
                        weatherForecastData.value = response.body()
                    } catch (ex: Exception) {
                        weatherForecastDataError.value = null
                    }
                } else {
                    weatherForecastDataError.value = response.errorBody()
                }
            }

            override fun onFailure(call: Call<APIsData>, t: Throwable) {
                loading.value = false
                weatherForecastDataError.value = null
            }
        })
    }

}