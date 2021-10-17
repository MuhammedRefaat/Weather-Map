package com.imagine.weathermap.services

import android.app.Activity
import com.imagine.weathermap.misc.AppConstants
import com.yodo1.mas.Yodo1Mas
import com.yodo1.mas.error.Yodo1MasError

class Yodo(activity: Activity) {

    var context: Activity = activity

    init {
        // init Yodo
        Yodo1Mas.getInstance()
            .init(context, AppConstants.YODO_APP_KEY, object : Yodo1Mas.InitListener {
                override fun onMasInitSuccessful() {
                    // Nothing to do for now
                }

                override fun onMasInitFailed(error: Yodo1MasError) {
                    // Nothing to do for now
                }
            })
    }

    fun displayBannerAd() {
        // Then Display
        Yodo1Mas.getInstance().showBannerAd(context)
    }

    fun displayInterstitialAd() {
        // Then Display
        Yodo1Mas.getInstance().showInterstitialAd(context)
    }

}