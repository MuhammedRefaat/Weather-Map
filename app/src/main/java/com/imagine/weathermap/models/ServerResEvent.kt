package com.imagine.weathermap.models

import okhttp3.ResponseBody

class ServerResEvent(
    var success: Boolean,
    var responseData: APIsData?,
    var errorBody: ResponseBody?,
    var cityName: String
)