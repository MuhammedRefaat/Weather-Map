package com.imagine.weathermap.views.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.imagine.weathermap.R
import com.imagine.weathermap.misc.Utils
import com.imagine.weathermap.services.Yodo
import eu.davidea.flipview.FlipView

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        const val SHARED_PREF_FILE = "Mango_Weather"
        const val IS_C = "is_c"
        var isC = true
        lateinit var cOrF: FlipView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(
            SHARED_PREF_FILE,
            Context.MODE_PRIVATE
        )
        isC = sharedPreferences.getBoolean(IS_C, true)
        cOrF = findViewById(R.id.c_or_f)
        cOrF.setOnClickListener(changeCF)
        setDegreeTypeButton()
        // Display Banner Ad
        Yodo(this).displayBannerAd()
    }

    override fun onResume() {
        super.onResume()
        setDegreeTypeButton()
    }

    private fun setDegreeTypeButton() {
        if (isC && cOrF.isFlipped) {
            cOrF.flip(false)
        } else if (!isC && !cOrF.isFlipped) {
            cOrF.flip(true)
        }
    }

    fun myCityWeather(view: View) {
        val intent = Intent(this, MyCityWeather::class.java)
        intent.putExtra(IS_C, isC)
        startActivity(intent)
    }

    fun otherCitiesWeather(view: View) {
        val intent = Intent(this, OtherCitiesWeather::class.java)
        intent.putExtra(IS_C, isC)
        startActivity(intent)
    }

    private val changeCF = View.OnClickListener { view ->
        isC = Utils.setCF()
        cOrF.flip(!isC)
    }


}
