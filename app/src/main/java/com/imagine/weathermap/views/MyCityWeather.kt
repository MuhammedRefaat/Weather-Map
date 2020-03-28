package com.imagine.weathermap.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.imagine.weathermap.controllers.Utils
import android.location.Geocoder
import android.os.Handler
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*
import com.imagine.weathermap.R
import com.imagine.weathermap.models.ServerResEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception


class MyCityWeather : AppCompatActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    lateinit var cityName: TextView
    lateinit var circleProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_city_weather)
        // declare views
        cityName = findViewById(R.id.city_name)
        circleProgress = findViewById(R.id.circular_progress)
        // Subscribe to EventBus events
        EventBus.getDefault().register(this)
        // Get My City Weather Details
        forecastMyCityWeather()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun forecastMyCityWeather() {
        if (Utils.locationPermissionsGranted(this)) {
            getCurrentLocation()
        } else {
            circleProgress.visibility = View.GONE
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
            getCityName(
                locationResult.lastLocation.latitude,
                locationResult.lastLocation.longitude
            )
            // TODO
            // lat = location.latitude
            // lon = location.longitude
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        circleProgress.visibility = View.VISIBLE
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (Utils.isLocationEnabled(this)) {
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    getCityName(location.latitude, location.longitude)
                    // TODO
                    // lat = location.latitude
                    // lon = location.longitude
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

    fun getCityName(latitude: Double, longitude: Double) {
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
        forecastMyCityWeather()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun serverResponseReceived(serverResEvent: ServerResEvent) {
        try {
            circleProgress.visibility = View.GONE
            if (serverResEvent.success) {
                // TODO
                serverResEvent.responseData
            } else {
                Toast.makeText(
                    this,
                    resources.getText(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}