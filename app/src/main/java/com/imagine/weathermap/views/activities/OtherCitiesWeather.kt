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
import androidx.core.view.children
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import com.imagine.weathermap.R
import com.imagine.weathermap.databinding.OtherCitiesWeatherBinding
import com.imagine.weathermap.views.customViews.WeatherDetails
import java.lang.Exception
import com.imagine.weathermap.misc.Utils
import com.imagine.weathermap.models.APIsData
import com.imagine.weathermap.models.CitySearchWeatherViewModel
import com.wajahatkarim3.easyflipview.EasyFlipView


class OtherCitiesWeather : AppCompatActivity() {

    lateinit var weatherViewModel: CitySearchWeatherViewModel
    lateinit var binding: OtherCitiesWeatherBinding
    lateinit var searchIcon: ImageView
    lateinit var searchField: EditText
    lateinit var errorText: TextView
    lateinit var circleProgress: ProgressBar
    lateinit var containerLayout: LinearLayout
    lateinit var emptyScreen: ImageView
    lateinit var cOrF: EasyFlipView
    private lateinit var citiesWeatherConditions: Map<String, APIsData>
    var isC = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OtherCitiesWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // declare views
        searchIcon = findViewById(R.id.search_icon)
        searchField = findViewById(R.id.search_field)
        errorText = findViewById(R.id.error_text)
        circleProgress = findViewById(R.id.circular_progress)
        containerLayout = findViewById(R.id.cities_weather)
        emptyScreen = findViewById(R.id.empty_screen_decoration)
        cOrF = findViewById(R.id.c_or_f)
        cOrF.setOnFlipListener { easyFlipView, newCurrentSide ->
            changeCFOtherCities(cOrF)
        }
        // get the measuring unit
        isC = intent.getBooleanExtra(MainActivity.IS_C, true)
        if (!isC)
            cOrF.flipTheView()
        // declaring the view model
        weatherViewModel = ViewModelProvider(this).get(CitySearchWeatherViewModel::class.java)
        observeWeatherViewModel()
        // add text watcher for the Search TextField
        searchField.addTextChangedListener(searchTextWatcher)
    }

    /**
     * View model observers subscribers for data handling
     */
    private fun observeWeatherViewModel() {
        // observer for the loading process
        weatherViewModel.loading.observe(this, androidx.lifecycle.Observer {
            if (it) {
                circleProgress.visibility = View.VISIBLE
            } else {
                circleProgress.visibility = View.GONE
            }
        })
        // observer for the data success result
        weatherViewModel.weatherDetailsDataError.observe(this, androidx.lifecycle.Observer {
            // get the New Errorn Data to handle/display
            for ((cityName, weatherConditionError) in it) {
                var cityIsThere = false
                // first check if city is already displayed in the layout
                for (singleCity in containerLayout.children) {
                    if ((singleCity as LinearLayout).tag == cityName) {
                        cityIsThere = true
                        break
                    }
                }
                // if city already displayed, no need to repaint
                if (cityIsThere)
                    continue
                // if not, go for it
                WeatherDetails(this@OtherCitiesWeather).buildOtherCitiesForecastLayout(
                    null,
                    cityName,
                    containerLayout,
                    isC
                )
            }
        })
        // observer for the data error
        weatherViewModel.weatherDetailsData.observe(this, androidx.lifecycle.Observer {
            try {
                // get the Data and display it
                citiesWeatherConditions = it
                displayWeatherData()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Toast.makeText(
                    this@OtherCitiesWeather, resources.getText(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun displayWeatherData() {
        for ((cityName, weatherCondition) in citiesWeatherConditions) {
            var cityIsThere = false
            // first check if city is already displayed in the layout
            for (singleCity in containerLayout.children) {
                if ((singleCity as LinearLayout).tag == cityName) {
                    cityIsThere = true
                    break
                }
            }
            // if city already displayed, no need to repaint
            if (cityIsThere)
                continue
            // if not, go for it
            WeatherDetails(this@OtherCitiesWeather).buildOtherCitiesForecastLayout(
                weatherCondition,
                cityName,
                containerLayout,
                isC
            )
        }
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
        if (cities.isEmpty()) { // No Search for you
            errorText.visibility = View.VISIBLE
            errorText.text = getString(R.string.cities_less)
            return
        } else {
            // Start Searching
            // hide empty screen
            emptyScreen.visibility = View.GONE
            // show progress
            circleProgress.visibility = View.VISIBLE
            // clear previous Data (if any)
            containerLayout.removeAllViews()
            // close the keyboard
            Utils.hideKeyboard(searchField, this@OtherCitiesWeather)
            // call the Server API
            weatherViewModel.getWeatherDetails(cities, Utils.getUnit(isC))
        }

    }

    fun changeCFOtherCities (view: View){
        isC = Utils.setCF()
        binding.cOrF.flipTheView(!isC)
        if (containerLayout.childCount > 0 && citiesWeatherConditions.isNotEmpty()) {
            containerLayout.removeAllViews()
            displayWeatherData()
        }
    }

}