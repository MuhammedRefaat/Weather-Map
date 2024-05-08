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

class CitySearchWeatherViewModel : ViewModel() {

    @Inject
    lateinit var weatherService: APIsCaller

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

    fun getWeatherDetails(cities: List<String>, unit: String) {
        weatherDetailsList.clear()
        weatherDetailsListError.clear()
        weatherDetailsData.value = weatherDetailsList
        weatherDetailsDataError.value = weatherDetailsListError
        getCitiesWeatherData(cities, unit)
    }

    private fun getCitiesWeatherData(cities: List<String>, unit: String) {
        for (city in cities) {
            weatherService.getCityWeatherCondition(city, unit).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<APIsData>() {
                    override fun onSuccess(response: APIsData?) {
                        loading.value = false
                        try {
                            weatherDetailsList[city] = response!!
                            weatherDetailsData.value = weatherDetailsList
                        } catch (ex: Exception) {
                            weatherDetailsListError[city] = null
                            weatherDetailsDataError.value = weatherDetailsListError
                        }
                    }

                    override fun onError(e: Throwable?) {
                        loading.value = false
                        weatherDetailsListError[city] = null
                        weatherDetailsDataError.value = weatherDetailsListError
                    }
                })
        }
    }

}