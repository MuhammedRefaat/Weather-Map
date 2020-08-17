package com.imagine.weathermap.services

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews

import com.imagine.weathermap.R
import com.imagine.weathermap.misc.TempController

class CurrentWeatherService : Service() {
    private var currentTemp = ""

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        updateMood(intent)
        stopSelf(startId)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun updateMood(intent: Intent?) {
        if (intent != null) {
            val requestedAction = intent.action
            if (requestedAction != null && requestedAction == UPDATE_WEATHER) {
                currentTemp = getCurrentTemp()
                val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
                val appWidgetMan = AppWidgetManager.getInstance(this)
                val views = RemoteViews(this.packageName, R.layout.app_widget)
                views.setTextViewText(R.id.current_temp, currentTemp)
                appWidgetMan.updateAppWidget(widgetId, views)
            }
        }
    }

    private fun getCurrentTemp(): String {
        var temp = ""
        try {
            temp = TempController.getCurrentTemp()
        } catch (ex: Exception) {
            print(ex.stackTrace)
        }
        return temp
    }

    companion object {
        const val UPDATE_WEATHER = "UpdatingWeather"
    }

}
