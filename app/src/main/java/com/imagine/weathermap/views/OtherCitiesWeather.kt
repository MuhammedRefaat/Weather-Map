package com.imagine.weathermap.views

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.imagine.weathermap.R

class OtherCitiesWeather : AppCompatActivity() {

    lateinit var searchIcon: ImageView
    lateinit var searchField: EditText
    lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.other_cities_weather)
        // declare views
        searchIcon = findViewById(R.id.search_icon)
        searchField = findViewById(R.id.search_field)
        errorText = findViewById(R.id.error_text)
        // add text watcher for the Search TextField
        searchField.addTextChangedListener(searchTextWatcher)
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
        if (cities.size < 3 || cities.size > 7) {
            errorText.visibility = View.VISIBLE
            if(cities.size < 3)
                errorText.text = getString(R.string.cities_less)
            else
                errorText.text = getString(R.string.cities_more)
            return
        }else{
            // TODO
        }

    }

}