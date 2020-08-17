package com.imagine.weathermap.views.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import com.imagine.weathermap.R
import com.imagine.weathermap.services.CurrentWeatherService

class CurrentWeatherWidgetProvider : AppWidgetProvider() {


    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for (i in appWidgetIds.indices) {
            val appWidgetId = appWidgetIds[i]
            val views = RemoteViews(context.packageName, R.layout.app_widget)
            val intent = Intent(context, CurrentWeatherService::class.java)
            intent.action = CurrentWeatherService.UPDATE_WEATHER
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.widgetBtn, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}
