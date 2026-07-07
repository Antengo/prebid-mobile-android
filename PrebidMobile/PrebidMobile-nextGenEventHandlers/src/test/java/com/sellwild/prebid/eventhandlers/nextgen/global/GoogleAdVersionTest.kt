package com.sellwild.prebid.eventhandlers.nextgen.global

import com.google.android.libraries.ads.mobile.sdk.MobileAds
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import com.sellwild.prebid.SellwildPrebid
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class GoogleAdVersionTest {
    @Test
    fun checkIfLastVersionUsed() {
        val currentVersion = MobileAds.getVersion().toString()
        Assert.assertEquals(
            "Google Next-Gen SDK was updated to " + currentVersion + "! " +
                "Please test Prebid SDK with the new version, resolve compilation problems, rewrite deprecated code. " +
                "After testing you must update SellwildPrebid.TESTED_GOOGLE_NEXT_GEN_SDK_VERSION " +
                "and also update version in publishing XML files (scripts/Maven/*.xml.)",
            currentVersion,
            SellwildPrebid.TESTED_GOOGLE_NEXT_GEN_SDK_VERSION
        )
    }
}
