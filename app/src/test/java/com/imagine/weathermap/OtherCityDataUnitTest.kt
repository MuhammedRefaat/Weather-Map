package com.imagine.weathermap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.imagine.weathermap.models.APIsData
import com.imagine.weathermap.models.CitySearchWeatherViewModel
import com.imagine.weathermap.network.APIsCaller
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import io.reactivex.android.plugins.RxAndroidPlugins
import org.junit.Assert
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class OtherCityDataUnitTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var apisCaller: APIsCaller

    @InjectMocks
    var citySearchWeatherViewModel = CitySearchWeatherViewModel()

    private var testOtherCityData: Single<APIsData>? = null


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testSuccessResults() {
        val gson = Gson()
        val cityJsonData =
            gson.fromJson(DataValidatorUnitTest.sampleOtherCityServerResponse, APIsData::class.java)
        testOtherCityData = Single.just(cityJsonData)

        Mockito.`when`(apisCaller.getCityWeatherCondition("", "imperial"))
            .thenReturn(testOtherCityData)

        val cities = ","
        citySearchWeatherViewModel.getWeatherDetails(cities.split(","), "imperial")

        Assert.assertEquals(1, citySearchWeatherViewModel.weatherDetailsList.size)
        Assert.assertEquals(null, citySearchWeatherViewModel.weatherDetailsDataError.value)
        Assert.assertEquals(false, citySearchWeatherViewModel.loading.value)
    }

    @Test
    fun testFailResults() {
        testOtherCityData = Single.error(Throwable())

        Mockito.`when`(apisCaller.getCityWeatherCondition("", "imperial"))
            .thenReturn(testOtherCityData)

        val cities = ","
        citySearchWeatherViewModel.getWeatherDetails(cities.split(","), "imperial")

        Assert.assertEquals(null, citySearchWeatherViewModel.weatherDetailsData.value)
        Assert.assertEquals(false, citySearchWeatherViewModel.loading.value)
    }


    @Before
    fun setUpRxSettings() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

}