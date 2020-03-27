package com.imagine.weathermap.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.imagine.weathermap.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun myCityWeather(view: View) {
        val intent = Intent(this, MyCityWeather::class.java)
        startActivity(intent)
    }

    fun otherCitiesWeather(view: View) {
        val intent = Intent(this, OtherCitiesWeather::class.java)
        startActivity(intent)
    }

}
