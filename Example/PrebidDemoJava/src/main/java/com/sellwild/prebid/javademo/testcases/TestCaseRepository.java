package com.sellwild.prebid.javademo.testcases;

import com.google.common.collect.Lists;

import com.sellwild.prebid.javademo.R;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiDisplayBanner300x250;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiDisplayBanner320x50;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiDisplayInterstitial;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiMultiformatBanner;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiMultiformatBannerVideoNativeInApp;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiMultiformatBannerVideoNativeStyles;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiMultiformatInterstitial;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiNativeInApp;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiNativeStyles;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiVideoBanner;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiVideoInStream;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiVideoInterstitial;
import com.sellwild.prebid.javademo.activities.ads.gam.original.GamOriginalApiVideoRewarded;

import java.util.ArrayList;

public class TestCaseRepository {

    public static TestCase lastTestCase;

    public static ArrayList<TestCase> getList() {
        ArrayList<TestCase> result = Lists.newArrayList(
            new TestCase(
                R.string.gam_original_display_banner_320x50,
                    AdFormat.DISPLAY_BANNER,
                    IntegrationKind.GAM_ORIGINAL,
                    GamOriginalApiDisplayBanner320x50.class
            ),
                new TestCase(
                        R.string.gam_original_display_banner_300x250,
                        AdFormat.DISPLAY_BANNER,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiDisplayBanner300x250.class
                ),
                new TestCase(
                        R.string.gam_original_multiformat_banner_300x250,
                        AdFormat.MULTIFORMAT,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiMultiformatBanner.class
                ),
                new TestCase(
                        R.string.gam_original_multiformat_banner_video_native_in_app,
                        AdFormat.MULTIFORMAT,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiMultiformatBannerVideoNativeInApp.class
                ),
                new TestCase(
                        R.string.gam_original_multiformat_banner_video_native_styles,
                        AdFormat.MULTIFORMAT,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiMultiformatBannerVideoNativeStyles.class
                ),
                new TestCase(
                        R.string.gam_original_video_banner,
                        AdFormat.VIDEO_BANNER,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiVideoBanner.class
                ),
                new TestCase(
                        R.string.gam_original_display_interstitial,
                        AdFormat.DISPLAY_INTERSTITIAL,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiDisplayInterstitial.class
                ),
                new TestCase(
                        R.string.gam_original_video_interstitial,
                        AdFormat.VIDEO_INTERSTITIAL,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiVideoInterstitial.class
                ),
                new TestCase(
                        R.string.gam_original_multiformat_interstitial,
                        AdFormat.VIDEO_INTERSTITIAL,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiMultiformatInterstitial.class
                ),
                new TestCase(
                        R.string.gam_original_video_rewarded,
                        AdFormat.VIDEO_REWARDED,
                        IntegrationKind.GAM_ORIGINAL,
                        GamOriginalApiVideoRewarded.class
                ),
                new TestCase(
                        R.string.gam_original_native_in_app,
                        AdFormat.NATIVE,
                        IntegrationKind.GAM_ORIGINAL,
                GamOriginalApiNativeInApp.class
            ),
            new TestCase(
                R.string.gam_original_native_styles,
                AdFormat.NATIVE,
                IntegrationKind.GAM_ORIGINAL,
                GamOriginalApiNativeStyles.class
            ),
            new TestCase(
                R.string.gam_original_in_stream,
                AdFormat.IN_STREAM_VIDEO,
                IntegrationKind.GAM_ORIGINAL,
                GamOriginalApiVideoInStream.class
            )
        );
        return result;
    }

}
