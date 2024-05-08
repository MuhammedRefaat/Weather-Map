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
import com.imagine.weathermap.databinding.SingleCityWeatherBinding
import com.imagine.weathermap.databinding.WeatherForecastBinding
import com.imagine.weathermap.misc.Utils
import com.imagine.weathermap.models.APIsData


class WeatherDetails : LinearLayout {

    private lateinit var binding: WeatherForecastBinding

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

    companion object {
        const val FEELS_LIKE: String = "Feels Like "
    }

    @SuppressLint("SetTextI18n")
    fun buildMyCityForecastLayout(
        weatherCondition: APIsData,
        containerLayout: LinearLayout,
        isC: Boolean
    ): LinearLayout {
        containerLayout.removeAllViews()
        val view = SingleCityWeatherBinding.inflate(LayoutInflater.from(context))
        try {
            view.cityName.visibility = View.GONE
            view.temperature.text = Utils.tempValue(weatherCondition.main!!.temp, isC)
            view.tempIcon.setImageResource(Utils.getAssetForTemp(weatherCondition.main.temp))
            view.feelsLike.text =
                FEELS_LIKE + Utils.tempValue(weatherCondition.main.feelsLike, isC)
            view.WeatherDesc.text = weatherCondition.weather!![0].description!!
            view.humidity.text = weatherCondition.main.humidity
            view.windSpeed.text = weatherCondition.wind!!.speed
            view.cloud.text = weatherCondition.weather[0].clouds!!
        } catch (ex: Exception) {
            ex.printStackTrace()
            view.weatherDetails.visibility = View.GONE
            view.na.visibility = View.VISIBLE
        }
        containerLayout.addView(view.root)
        return containerLayout
    }

    @SuppressLint("SetTextI18n")
    fun buildOtherCitiesForecastLayout(
        weatherCondition: APIsData?,
        cityName: String,
        containerLayout: LinearLayout,
        isC: Boolean
    ): LinearLayout {
        val view = SingleCityWeatherBinding.inflate(LayoutInflater.from(context))
        if (weatherCondition != null) {
            try {
                view.cityName.text = weatherCondition.name
                view.temperature.text = Utils.tempValue(weatherCondition.main!!.temp, isC)
                view.tempIcon.setImageResource(Utils.getAssetForTemp(weatherCondition.main.temp))
                view.feelsLike.text =
                    FEELS_LIKE + Utils.tempValue(weatherCondition.main.feelsLike, isC)
                view.WeatherDesc.text = weatherCondition.weather!![0].description!!
                view.windSpeed.text = weatherCondition.wind!!.speed
                view.humidity.text = weatherCondition.main.humidity
                view.cloud.text = weatherCondition.weather[0].clouds!!
            } catch (ex: Exception) {
                ex.printStackTrace()
                view.weatherDetails.visibility = View.GONE
                view.na.visibility = View.VISIBLE
            }
        } else {
            view.cityName.text = cityName
            view.weatherDetails.visibility = View.GONE
            view.na.visibility = View.VISIBLE
        }
        view.root.tag = cityName
        containerLayout.addView(view.root)
        return containerLayout
    }

}