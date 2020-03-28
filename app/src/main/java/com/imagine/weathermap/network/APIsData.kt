package com.imagine.weathermap.network

import com.google.gson.annotations.SerializedName

/**
 * Created by Muhammed Refaat on 28/3/2020
 */

class APIsData {

    @SerializedName("name")
    val name: String = ""

    @SerializedName("main")
    val main: Main? = null

    @SerializedName("weather")
    val weather: List<Weather>? = null

    @SerializedName("wind")
    val wind: Wind? = null

    inner class Main {

        @SerializedName("temp")
        val temp: String? = null

        @SerializedName("feels_like")
        val feelsLike: String? = null

        @SerializedName("temp_min")
        val tempMin: String? = null

        @SerializedName("temp_max")
        val tempMax: String? = null

        @SerializedName("pressure")
        val pressure: String? = null

        @SerializedName("humidity")
        val humidity: String? = null

    }

    inner class Weather {

        @SerializedName("description")
        val description: String? = null

    }

    inner class Wind {

        @SerializedName("speed")
        val speed: String? = null

    }
}