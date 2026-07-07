package com.sellwild.prebid.api.original;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import com.sellwild.prebid.AdSize;
import com.sellwild.prebid.AdUnit;
import com.sellwild.prebid.BannerParameters;
import com.sellwild.prebid.NativeParameters;
import com.sellwild.prebid.OnCompleteListener;
import com.sellwild.prebid.ResultCode;
import com.sellwild.prebid.Util;
import com.sellwild.prebid.VideoParameters;
import com.sellwild.prebid.api.data.AdFormat;
import com.sellwild.prebid.api.exceptions.AdException;
import com.sellwild.prebid.configuration.AdUnitConfiguration;
import com.sellwild.prebid.configuration.NativeAdUnitConfiguration;
import com.sellwild.prebid.rendering.bidding.data.bid.BidResponse;
import com.sellwild.prebid.rendering.bidding.listeners.BidRequesterListener;
import com.sellwild.prebid.rendering.models.AdPosition;
import com.sellwild.prebid.rendering.models.PlacementType;

import java.util.HashMap;

/**
 * Internal AdUnit implementation that is used for PrebidAdUnit
 * with multi-format configuration. It separates logic for multi-format ad unit.
 */
class MultiformatAdUnitFacade extends AdUnit {

    @Nullable
    private BidResponse bidResponse;

    public MultiformatAdUnitFacade(@NotNull String configId, @NonNull PrebidRequest request) {
        super(configId);
        allowNullableAdObject = true;
        setConfigurationBasedOnRequest(request);
    }

    /**
     * Applies the interstitial native visibility tracker for tracking `burl` url.
     */
    public void activateInterstitialPrebidImpressionTracker() {
        this.activateInterstitialPrebidImpressionTracker = true;
    }

    @Override
    protected BidRequesterListener createBidListener(OnCompleteListener originalListener) {
        return new BidRequesterListener() {
            @Override
            public void onFetchCompleted(BidResponse response) {
                bidResponse = response;

                HashMap<String, String> keywords = response.getTargeting();
                Util.apply(keywords, adObject);

                originalListener.onComplete(ResultCode.SUCCESS);
            }

            @Override
            public void onError(AdException exception) {
                bidResponse = null;

                Util.apply(null, adObject);

                originalListener.onComplete(convertToResultCode(exception));
            }
        };
    }

    private void setConfigurationBasedOnRequest(
            @NonNull PrebidRequest request
    ) {

        BannerParameters bannerParameters = request.getBannerParameters();
        if (bannerParameters != null) {
            if (request.isInterstitial()) {
                configuration.addAdFormat(AdFormat.INTERSTITIAL);

                Integer minWidth = bannerParameters.getInterstitialMinWidthPercentage();
                Integer minHeight = bannerParameters.getInterstitialMinHeightPercentage();
                if (minWidth != null && minHeight != null) {
                    configuration.setMinSizePercentage(new AdSize(minWidth, minHeight));
                }
            } else {
                configuration.addAdFormat(AdFormat.BANNER);
            }

            configuration.setBannerParameters(bannerParameters);
            configuration.addSizes(bannerParameters.getAdSizes());
        }

        VideoParameters videoParameters = request.getVideoParameters();
        if (videoParameters != null) {
            configuration.addAdFormat(AdFormat.VAST);

            if (request.isInterstitial()) {
                configuration.setPlacementType(PlacementType.INTERSTITIAL);
            }
            if (request.isRewarded()) {
                configuration.setRewarded(true);
            }

            configuration.setVideoParameters(videoParameters);
            configuration.addSize(videoParameters.getAdSize());
        }

        NativeParameters nativeParameters = request.getNativeParameters();
        if (nativeParameters != null) {
            configuration.addAdFormat(AdFormat.NATIVE);

            NativeAdUnitConfiguration nativeConfig = nativeParameters.getNativeConfiguration();
            configuration.setNativeConfiguration(nativeConfig);
        }

        String gpid = request.getGpid();
        configuration.setGpid(gpid);

        if (request.isInterstitial()) {
            configuration.setAdPosition(AdPosition.FULLSCREEN);
        } else {
            configuration.setAdPosition(request.getAdPosition());
        }
    }

    @Nullable
    public BidResponse getBidResponse() {
        return bidResponse;
    }

    @SuppressLint("VisibleForTests")
    @Override
    public AdUnitConfiguration getConfiguration() {
        return super.getConfiguration();
    }

}