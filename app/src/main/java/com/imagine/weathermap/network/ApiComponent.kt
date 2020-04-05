package com.imagine.weathermap.network

import com.imagine.weathermap.models.CitySearchWeatherViewModel
import com.imagine.weathermap.models.MyCityForecastViewModel
import dagger.Component

@Component(modules = [APIsClient::class])
interface ApiComponent {

    fun inject(service: APIsCaller)

    fun inject(viewModel: MyCityForecastViewModel)

    fun inject(viewModel: CitySearchWeatherViewModel)

}