package com.imagine.weathermap.views.activities

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.imagine.weathermap.R
import com.imagine.weathermap.models.ServerResEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class OtherCitiesWeather : AppCompatActivity() {

    lateinit var searchIcon: ImageView
    lateinit var searchField: EditText
    lateinit var errorText: TextView
    lateinit var circleProgress : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.other_cities_weather)
        // declare views
        searchIcon = findViewById(R.id.search_icon)
        searchField = findViewById(R.id.search_field)
        errorText = findViewById(R.id.error_text)
        circleProgress = findViewById(R.id.circular_progress)
        // add text watcher for the Search TextField
        searchField.addTextChangedListener(searchTextWatcher)
        // Subscribe to EventBus events
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    /**
     * Text Watcher for search field
     */
    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            if (searchField.text.toString().isEmpty()) {
                searchIcon.setTint(this@OtherCitiesWeather, R.color.gray)
            } else {
                searchIcon.setTint(
                    this@OtherCitiesWeather,
                    R.color.colorPrimary
                )
                if(searchField.text.split(",").size > 7) {
                    errorText.text = getString(R.string.cities_more)
                    errorText.visibility = View.VISIBLE
                }else{
                    errorText.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun ImageView.setTint(context: Context, @ColorRes colorId: Int) {
        val color = ContextCompat.getColor(context, colorId)
        val colorStateList = ColorStateList.valueOf(color)
        ImageViewCompat.setImageTintList(this, colorStateList)
    }

    fun goBack(view: View) {
        super.onBackPressed()
    }

    fun searchCities(view: View) {
        // give press feeling
        view.alpha = 0.3f
        Handler().postDelayed({
            view.alpha = 1.0f
        }, 150)
        // do the work
        val cities = searchField.text.split(",")
        if (cities.size < 3 || cities.size > 7) { // No Search for you
            errorText.visibility = View.VISIBLE
            if(cities.size < 3)
                errorText.text = getString(R.string.cities_less)
            else
                errorText.text = getString(R.string.cities_more)
            return
        }else{
            // Start Searching
            // show progress
            circleProgress.visibility = View.VISIBLE
            // call the server
            // TODO
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun serverResponseReceived(serverResEvent: ServerResEvent) {
        try {
            circleProgress.visibility = View.GONE
            if (serverResEvent.success) {
                // TODO
                serverResEvent.responseData
            } else {
                Toast.makeText(
                    this,
                    resources.getText(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}