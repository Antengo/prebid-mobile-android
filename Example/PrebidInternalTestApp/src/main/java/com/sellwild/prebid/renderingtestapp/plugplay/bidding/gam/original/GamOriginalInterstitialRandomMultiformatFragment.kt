package com.sellwild.prebid.renderingtestapp.plugplay.bidding.gam.original

import com.sellwild.prebid.AdUnit
import com.sellwild.prebid.InterstitialAdUnit
import com.sellwild.prebid.VideoParameters
import com.sellwild.prebid.api.data.AdUnitFormat
import com.sellwild.prebid.renderingtestapp.R
import java.util.*
import kotlin.random.Random

class GamOriginalInterstitialRandomMultiformatFragment : GamOriginalInterstitialFragment() {

    override fun createAdUnit(adUnitFormat: AdUnitFormat): AdUnit {
        val configId =
            if (Random.nextBoolean()) getString(R.string.imp_prebid_id_interstitial_320_480) else getString(R.string.imp_prebid_id_video_interstitial_320_480_original_api)
        val adUnit = InterstitialAdUnit(configId, EnumSet.of(AdUnitFormat.BANNER, AdUnitFormat.VIDEO))
        adUnit.videoParameters = VideoParameters(listOf("video/mp4"))
        return adUnit
    }

}