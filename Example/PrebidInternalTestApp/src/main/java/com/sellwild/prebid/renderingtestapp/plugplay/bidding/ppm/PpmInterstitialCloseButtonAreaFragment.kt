package com.sellwild.prebid.renderingtestapp.plugplay.bidding.ppm

import com.sellwild.prebid.api.data.AdUnitFormat
import com.sellwild.prebid.api.rendering.InterstitialAdUnit
import com.sellwild.prebid.renderingtestapp.plugplay.bidding.base.BaseBidInterstitialFragment
import com.sellwild.prebid.api.data.Position
import java.util.*

class PpmInterstitialCloseButtonAreaFragment : BaseBidInterstitialFragment() {

    override fun initInterstitialAd(
        adUnitFormat: AdUnitFormat,
        adUnitId: String?,
        configId: String?,
        width: Int,
        height: Int
    ) {
        interstitialAdUnit = if (adUnitFormat == AdUnitFormat.VIDEO) {
            InterstitialAdUnit(
                requireContext(),
                configId,
                EnumSet.of(adUnitFormat)
            )
        } else {
            InterstitialAdUnit(requireContext(), configId)
        }
        interstitialAdUnit?.setInterstitialAdUnitListener(this)
        interstitialAdUnit?.setCloseButtonArea(0.40)
        interstitialAdUnit?.setCloseButtonPosition(Position.TOP_LEFT)
    }

}