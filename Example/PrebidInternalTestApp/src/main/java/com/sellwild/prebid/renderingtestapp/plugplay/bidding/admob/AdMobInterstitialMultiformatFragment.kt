package com.sellwild.prebid.renderingtestapp.plugplay.bidding.admob

import android.os.Bundle
import com.sellwild.prebid.admob.AdMobMediationInterstitialUtils
import com.sellwild.prebid.api.data.AdUnitFormat
import com.sellwild.prebid.api.mediation.MediationInterstitialAdUnit
import com.sellwild.prebid.renderingtestapp.R
import java.util.*

open class AdMobInterstitialMultiformatFragment : AdMobInterstitialFragment() {

    override fun initAd(): Any? {
        val context = requireContext()

        extras = Bundle()
        val mediationUtils = AdMobMediationInterstitialUtils(extras)
        val adUnitFormats = EnumSet.of(AdUnitFormat.BANNER, AdUnitFormat.VIDEO)
        adUnit = MediationInterstitialAdUnit(
            activity,
            listOf(
                context.getString(R.string.imp_prebid_id_interstitial_320_480),
                context.getString(R.string.imp_prebid_id_video_interstitial_320_480)
            ).shuffled().first(),
            adUnitFormats,
            mediationUtils
        )
        adUnit?.setMinSizePercentage(30, 30)
        return adUnit
    }

}