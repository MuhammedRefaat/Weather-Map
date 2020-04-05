package com.imagine.weathermap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.imagine.weathermap.models.APIsData
import com.imagine.weathermap.models.MyCityForecastViewModel
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

class MyCityDataUnitTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var apisCaller: APIsCaller

    @InjectMocks
    var myCityForecastViewModel = MyCityForecastViewModel()

    private var testMyCityData: Single<APIsData>? = null


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testSuccessResults() {
        val gson = Gson()
        val myCityJsonData =
            gson.fromJson(DataValidatorUnitTest.sampleMyCityServerResponse, APIsData::class.java)
        testMyCityData = Single.just(myCityJsonData)

        Mockito.`when`(apisCaller.getMyCityWeatherForecast("30.05", lon = "31.24"))
            .thenReturn(testMyCityData)

        myCityForecastViewModel.getWeatherForecast(lat = "30.05", lon = "31.24")

        Assert.assertTrue(
            myCityForecastViewModel.weatherForecastData.value?.weatherConditions!![0]
                .main?.temp is String
        )
        Assert.assertEquals(
            "Mountain View",
            myCityForecastViewModel.weatherForecastData.value?.city?.name
        )
        Assert.assertEquals(null, myCityForecastViewModel.weatherForecastDataError.value)
        Assert.assertEquals(false, myCityForecastViewModel.loading.value)
    }

    @Test
    fun testFailResults() {
        testMyCityData = Single.error(Throwable())

        Mockito.`when`(apisCaller.getMyCityWeatherForecast("", "")).thenReturn(testMyCityData)

        myCityForecastViewModel.getWeatherForecast(lat = "", lon = "")

        Assert.assertEquals(null, myCityForecastViewModel.weatherForecastData.value)
        Assert.assertEquals(false, myCityForecastViewModel.loading.value)
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