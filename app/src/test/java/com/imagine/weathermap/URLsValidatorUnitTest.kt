package com.imagine.weathermap

import org.junit.Test
import org.junit.Assert.*
import com.imagine.weathermap.misc.AppConstants
import java.net.URI
import java.net.URL

class URLsValidatorUnitTest {

    @Test
    fun myCityCallUrlValidator_ReturnsTrue() {
        assertTrue(
            URL(AppConstants.APIS_URL + AppConstants.MY_CITY_API_URL_EXT).toURI()
                    is URI
        )
    }

    @Test
    fun otherCitiesCallUrlValidator_ReturnsTrue() {
        assertTrue(
            URL(AppConstants.APIS_URL + AppConstants.OTHER_CITIES_API_URL_EXT).toURI()
                    is URI
        )
    }

}