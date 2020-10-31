package com.imagine.weathermap.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagine.weathermap.network.APIsCaller
import com.imagine.weathermap.network.APIsClient
import com.imagine.weathermap.network.DaggerApiComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import javax.inject.Inject

class MyCityForecastViewModel : ViewModel() {

    @Inject
    lateinit var weatherService: APIsCaller

    private lateinit var latitude: String

    private lateinit var longitude: String

    private lateinit var unit: String

    val weatherForecastData = MutableLiveData<APIsData>()

    val weatherForecastDataError = MutableLiveData<ResponseBody>()

    val loading = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.builder()
            .aPIsClient(APIsClient)
            .build().inject(this)
    }

    fun getWeatherForecast(lat: String, lon: String, unit: String) {
        this.latitude = lat
        this.longitude = lon
        this.unit = unit
        getMyCityWeatherForecast()
    }

    private fun getMyCityWeatherForecast() {
        weatherService.getMyCityWeatherForecast(latitude, longitude, unit)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<APIsData>() {
                override fun onSuccess(response: APIsData?) {
                    loading.value = false
                    try {
                        weatherForecastData.value = response
                    } catch (ex: Exception) {
                        weatherForecastDataError.value = null
                    }
                }

                override fun onError(e: Throwable?) {
                    loading.value = false
                    weatherForecastDataError.value = null
                }
            })
    }

}