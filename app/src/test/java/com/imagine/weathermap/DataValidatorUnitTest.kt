package com.imagine.weathermap

import com.imagine.weathermap.models.APIsData
import org.junit.Test
import org.junit.Assert.*
import com.google.gson.Gson

class DataValidatorUnitTest {

    private val sampleMyCityServerResponse: String = "{\n" +
            "    \"cod\": \"200\",\n" +
            "    \"message\": 0,\n" +
            "    \"cnt\": 40,\n" +
            "    \"list\": [\n" +
            "        {\n" +
            "            \"dt\": 1585515600,\n" +
            "            \"main\": {\n" +
            "                \"temp\": 286.56,\n" +
            "                \"feels_like\": 285.13,\n" +
            "                \"temp_min\": 285.96,\n" +
            "                \"temp_max\": 286.56,\n" +
            "                \"pressure\": 1020,\n" +
            "                \"sea_level\": 1020,\n" +
            "                \"grnd_level\": 1020,\n" +
            "                \"humidity\": 69,\n" +
            "                \"temp_kf\": 0.6\n" +
            "            },\n" +
            "            \"weather\": [\n" +
            "                {\n" +
            "                    \"id\": 500,\n" +
            "                    \"main\": \"Rain\",\n" +
            "                    \"description\": \"light rain\",\n" +
            "                    \"icon\": \"10d\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"clouds\": {\n" +
            "                \"all\": 99\n" +
            "            },\n" +
            "            \"wind\": {\n" +
            "                \"speed\": 1.33,\n" +
            "                \"deg\": 144\n" +
            "            },\n" +
            "            \"rain\": {\n" +
            "                \"3h\": 1.56\n" +
            "            },\n" +
            "            \"sys\": {\n" +
            "                \"pod\": \"d\"\n" +
            "            },\n" +
            "            \"dt_txt\": \"2020-03-29 21:00:00\"\n" +
            "        },\n" +
            "    ],\n" +
            "    \"city\": {\n" +
            "        \"id\": 5375480,\n" +
            "        \"name\": \"Mountain View\",\n" +
            "        \"coord\": {\n" +
            "            \"lat\": 37.3861,\n" +
            "            \"lon\": -122.0838\n" +
            "        },\n" +
            "        \"country\": \"US\",\n" +
            "        \"population\": 74066,\n" +
            "        \"timezone\": -25200,\n" +
            "        \"sunrise\": 1585490235,\n" +
            "        \"sunset\": 1585535295\n" +
            "    }\n" +
            "}"

    private val sampleOtherCityServerResponse: String = "{\n" +
            "    \"coord\": {\n" +
            "        \"lon\": 31.24,\n" +
            "        \"lat\": 30.05\n" +
            "    },\n" +
            "    \"weather\": [\n" +
            "        {\n" +
            "            \"id\": 801,\n" +
            "            \"main\": \"Clouds\",\n" +
            "            \"description\": \"few clouds\",\n" +
            "            \"icon\": \"02n\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"base\": \"stations\",\n" +
            "    \"main\": {\n" +
            "        \"temp\": 287.09,\n" +
            "        \"feels_like\": 285.55,\n" +
            "        \"temp_min\": 287.04,\n" +
            "        \"temp_max\": 287.15,\n" +
            "        \"pressure\": 1016,\n" +
            "        \"humidity\": 67\n" +
            "    },\n" +
            "    \"visibility\": 10000,\n" +
            "    \"wind\": {\n" +
            "        \"speed\": 1.5,\n" +
            "        \"deg\": 60\n" +
            "    },\n" +
            "    \"clouds\": {\n" +
            "        \"all\": 20\n" +
            "    },\n" +
            "    \"dt\": 1585447077,\n" +
            "    \"sys\": {\n" +
            "        \"type\": 1,\n" +
            "        \"id\": 2514,\n" +
            "        \"country\": \"EG\",\n" +
            "        \"sunrise\": 1585453654,\n" +
            "        \"sunset\": 1585498296\n" +
            "    },\n" +
            "    \"timezone\": 7200,\n" +
            "    \"id\": 360630,\n" +
            "    \"name\": \"Cairo\",\n" +
            "    \"cod\": 200\n" +
            "}"

    @Test
    fun myCityDataValidator_ReturnsTrue() {
        val gson = Gson()
        val myCityJsonData = gson.fromJson(sampleMyCityServerResponse, APIsData::class.java)
        assertTrue(myCityJsonData.weatherConditions is List<APIsData.WeatherCondition>)
    }

    @Test
    fun otherCitiesDataValidator_ReturnsTrue() {
        val gson = Gson()
        val myCityJsonData = gson.fromJson(sampleOtherCityServerResponse, APIsData::class.java)
        assertTrue(myCityJsonData.weather is List<APIsData.WeatherCondition.Weather>)
    }

}