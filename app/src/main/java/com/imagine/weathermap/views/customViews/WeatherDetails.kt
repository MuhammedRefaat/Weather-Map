package com.imagine.weathermap.views.customViews

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.imagine.weathermap.R
import com.imagine.weathermap.models.APIsData
import kotlinx.android.synthetic.main.single_city_weather.view.*


class WeatherDetails : LinearLayout {
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    )
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    )
            : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        orientation = VERTICAL
    }

    @SuppressLint("SetTextI18n")
    fun buildMyCityForecastLayout(
        weatherCondition: APIsData,
        containerLayout: LinearLayout
    ): LinearLayout {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.single_city_weather, this, false)
        try {
            view.city_name.visibility = View.GONE
            view.temperature_min.text = "Temp " + weatherCondition.main!!.temp
            view.temperature_max.text = "Feels Like " + weatherCondition.main.feelsLike
            view.Weather_desc.text = weatherCondition.weather!![0].description!!
            view.wind_speed.text = weatherCondition.wind!!.speed
        } catch (ex: Exception) {
            ex.printStackTrace()
            view.weather_details.visibility = View.GONE
            view.na.visibility = View.VISIBLE
        }
        containerLayout.addView(view)
        return containerLayout
    }

    @SuppressLint("SetTextI18n")
    fun buildOtherCitiesForecastLayout(
        weatherCondition: APIsData?,
        cityName: String,
        containerLayout: LinearLayout
    ): LinearLayout {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.single_city_weather, this, false)
        if (weatherCondition != null) {
            try {
                view.city_name.text = weatherCondition.name
                view.temperature_min.text = "Min. " + weatherCondition.main!!.tempMin
                view.temperature_max.text = "Max. " + weatherCondition.main.tempMax
                view.Weather_desc.text = weatherCondition.weather!![0].description!!
                view.wind_speed.text = weatherCondition.wind!!.speed
            } catch (ex: Exception) {
                ex.printStackTrace()
                view.weather_details.visibility = View.GONE
                view.na.visibility = View.VISIBLE
            }
        } else {
            view.city_name.text = cityName
            view.weather_details.visibility = View.GONE
            view.na.visibility = View.VISIBLE
        }
        view.tag = cityName
        containerLayout.addView(view)
        return containerLayout
    }

}