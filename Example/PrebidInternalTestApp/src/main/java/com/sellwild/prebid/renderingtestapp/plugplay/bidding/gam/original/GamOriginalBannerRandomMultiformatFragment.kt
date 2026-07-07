package com.sellwild.prebid.renderingtestapp.plugplay.bidding.gam.original

import com.sellwild.prebid.BannerAdUnit
import com.sellwild.prebid.VideoParameters
import com.sellwild.prebid.api.data.AdUnitFormat
import com.sellwild.prebid.renderingtestapp.R
import java.util.*
import kotlin.random.Random

class GamOriginalBannerRandomMultiformatFragment : GamOriginalBannerFragment() {

    override fun createAdUnit(): BannerAdUnit {
        val configId =
            if (Random.nextBoolean()) getString(R.string.imp_prebid_id_banner_300x250) else getString(R.string.imp_prebid_id_video_outstream_original_api)
        val adUnit = BannerAdUnit(configId, width, height, EnumSet.of(AdUnitFormat.BANNER, AdUnitFormat.VIDEO))
        adUnit.videoParameters = VideoParameters(listOf("video/mp4"))
        return adUnit
    }

}