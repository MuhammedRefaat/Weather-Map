package com.imagine.weathermap.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.imagine.weathermap.misc.Utils
import android.location.Geocoder
import android.os.Handler
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import java.util.*
import com.imagine.weathermap.R
import com.imagine.weathermap.models.MyCityForecastViewModel
import com.imagine.weathermap.views.customViews.WeatherDetails
import okhttp3.ResponseBody
import java.lang.Exception


class MyCityWeather : AppCompatActivity() {

    lateinit var weatherViewModel: MyCityForecastViewModel

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    lateinit var cityName: TextView
    private lateinit var circleProgress: ProgressBar
    lateinit var containerLayout: LinearLayout
    lateinit var emptyScreen: ImageView

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_city_weather)
        // declare views
        cityName = findViewById(R.id.city_name)
        circleProgress = findViewById(R.id.circular_progress)
        containerLayout = findViewById(R.id.cities_weather)
        emptyScreen = findViewById(R.id.empty_screen_decoration)
        // declaring the view model and it's observer
        weatherViewModel = ViewModelProvider(this).get(MyCityForecastViewModel::class.java)
        observeWeatherViewModel()
        // Get My City Weather Details
        forecastMyCityWeather()
    }

    /**
     * View model observers subscribers for data handling
     */
    private fun observeWeatherViewModel() {
        // observer for the loading process
        weatherViewModel.loading.observe(this, androidx.lifecycle.Observer {
            if (it) {
                circleProgress.visibility = View.VISIBLE
            } else {
                circleProgress.visibility = View.GONE
            }
        })
        // observer for the data success result
        weatherViewModel.weatherForecastDataError.observe(this, androidx.lifecycle.Observer {
            goForError(it)
        })
        // observer for the data error
        weatherViewModel.weatherForecastData.observe(this, androidx.lifecycle.Observer {
            try {
                // get the Data and display it
                cityName.text = it?.city?.name
                val weatherConditions = it?.weatherConditions
                WeatherDetails(this@MyCityWeather).buildMyCityForecastLayout(
                    weatherConditions!!,
                    containerLayout
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
                Toast.makeText(
                    this@MyCityWeather, resources.getText(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun forecastMyCityWeather() {
        if (Utils.locationPermissionsGranted(this)) {
            getCurrentLocation()
        } else {
            circleProgress.visibility = View.GONE
            emptyScreen.visibility = View.VISIBLE
            Utils.requestPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Utils.LOCATION_PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation()
            } else {
                emptyScreen.visibility = View.VISIBLE
                Toast.makeText(
                    this,
                    resources.getText(R.string.give_location_permissions),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            latitude = locationResult.lastLocation.latitude
            longitude = locationResult.lastLocation.latitude
            // call the Server API
            weatherViewModel.getWeatherForecast(
                latitude.toString(),
                longitude.toString()
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        circleProgress.visibility = View.VISIBLE
        emptyScreen.visibility = View.GONE
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (Utils.isLocationEnabled( this)) {
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    latitude = location.latitude
                    longitude = location.longitude
                    // call the Server API
                    weatherViewModel.getWeatherForecast(
                        latitude.toString(),
                        longitude.toString()
                    )
                }
            }
        } else {
            circleProgress.visibility = View.GONE
            Toast.makeText(
                this, resources.getText(R.string.location_closed),
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    private fun getCityName(latitude: Double, longitude: Double) {
        val gcd = Geocoder(this@MyCityWeather, Locale.getDefault())
        val addresses = gcd.getFromLocation(latitude, longitude, 1)
        if (addresses.size > 0)
            cityName.text = addresses[0].locality
    }

    fun goBack(view: View) {
        super.onBackPressed()
    }

    fun reloadProcess(view: View) {
        // give press feeling
        view.alpha = 0.3f
        Handler().postDelayed({
            view.alpha = 1.0f
        }, 150)
        // do the work
        cityName.text = ""
        containerLayout.removeAllViews()
        forecastMyCityWeather()
    }

    private fun goForError(errorBody: ResponseBody?) {
        Utils.displayError(this@MyCityWeather, errorBody)
        emptyScreen.visibility = View.VISIBLE
        if (latitude != 0.0 && longitude != 0.0) {
            getCityName(latitude, longitude)
        }
    }
}