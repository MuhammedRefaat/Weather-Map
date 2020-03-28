package com.imagine.weathermap.controllers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.imagine.weathermap.R
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.Exception


class Utils {

    companion object {

        var LOCATION_PERMISSION_ID: Int = 0x100

        fun locationPermissionsGranted(context: Context): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }

        fun requestPermissions(context: Activity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_ID
            )
        }

        fun isLocationEnabled(context: Context): Boolean {
            val locationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

        fun displayError(context: Context, errorBody: ResponseBody?) {
            if (errorBody != null) {
                try {
                    val jObjError = JSONObject(errorBody.string())
                    Toast.makeText(
                        context, jObjError.getString("message"),
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        context.resources.getText(com.imagine.weathermap.R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else {
                Toast.makeText(
                    context, context.resources.getText(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}