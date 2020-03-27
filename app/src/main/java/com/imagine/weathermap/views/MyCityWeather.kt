package com.imagine.weathermap.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.imagine.weathermap.R

class MyCityWeather  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_city_weather)
    }

    fun goBack(view: View) {
        super.onBackPressed()
    }

}