package com.imagine.weathermap.misc

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.imagine.weathermap.R
import com.imagine.weathermap.views.activities.MainActivity
import com.imagine.weathermap.views.activities.MainActivity.Companion.IS_C
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode


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


        /**
         * To hide the keyboard when the user action Done
         */
        fun hideKeyboard(view: View?, context: Context) {
            try {
                // Check if no view has focus:
                if (view != null) {
                    val imm = context.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }


        /**
         * to set the shared preferences value for C or F degree type set
         */
        fun setCF(): Boolean {
            try {
                val isC = !MainActivity.sharedPreferences.getBoolean(IS_C, true)
                val editor: SharedPreferences.Editor = MainActivity.sharedPreferences.edit()
                editor.putBoolean(IS_C, isC)
                editor.apply()
                editor.commit()
                MainActivity.isC = isC
                return isC
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return true;
        }

        // setting the temperature as if it's in C or F format
        fun tempValue(temp: String?, isC: Boolean): CharSequence? {
            lateinit var tempValue: CharSequence
            tempValue = temp.toString()
            if (isC) {
                tempValue = (BigDecimal((5.0 / 9.0) * (temp!!.toDoubleOrNull()!! - 32.0)).setScale(
                    2,
                    RoundingMode.HALF_EVEN
                )).toString()
            }
            return tempValue
        }

        fun getUnit(isC: Boolean): String {
            return "imperial"
            /*return if (isC)
                "metric"
            else
                "imperial"*/
        }

        fun getAssetForTemp(temp: String?): Int {
            var asset: Int = R.drawable.ic_temperature
            try {
                val tempValue: Double = tempValue(temp, true)!!.toString().toDoubleOrNull()!!
                when {
                    tempValue >= 40 -> {
                        asset = R.drawable.ic_temperature_sunny
                    }
                    tempValue >= 30 -> {
                        asset = R.drawable.ic_temperature_hot
                    }
                    tempValue >= 10 -> {
                        asset = R.drawable.ic_temperature
                    }
                    tempValue < 10 -> {
                        asset = R.drawable.ic_temperature_cold
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return asset;
        }

    }

}