package com.sellwild.prebid.admob;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationBannerAd;
import com.google.android.gms.ads.mediation.MediationBannerAdCallback;
import com.google.android.gms.ads.mediation.MediationBannerAdConfiguration;

import com.sellwild.prebid.api.data.AdFormat;
import com.sellwild.prebid.api.exceptions.AdException;
import com.sellwild.prebid.api.rendering.DisplayView;
import com.sellwild.prebid.configuration.AdUnitConfiguration;
import com.sellwild.prebid.rendering.bidding.data.bid.BidResponse;
import com.sellwild.prebid.rendering.bidding.display.BidResponseCache;
import com.sellwild.prebid.rendering.bidding.listeners.DisplayViewListener;

/**
 * Prebid banner adapter for AdMob integration.
 */
public class PrebidBannerAdapter extends PrebidBaseAdapter {

    public static final String EXTRA_RESPONSE_ID = "PrebidBannerAdapterExtraId";

    private DisplayView adView;
    @Nullable
    private MediationBannerAdCallback adMobBannerListener;

    @Override
    public void loadBannerAd(
            @NonNull MediationBannerAdConfiguration configuration,
            @NonNull MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> adMobLoadListener
    ) {
        String responseId = getResponseIdAndCheckParameters(
                configuration,
                EXTRA_RESPONSE_ID,
                adMobLoadListener::onFailure
        );
        if (responseId == null) {
            return;
        }

        BidResponse response = BidResponseCache.getInstance().popBidResponse(responseId);
        if (response == null) {
            adMobLoadListener.onFailure(AdErrors.noResponse(responseId));
            return;
        }

        AdUnitConfiguration adConfiguration = new AdUnitConfiguration();
        adConfiguration.setAdFormat(AdFormat.BANNER);
        DisplayViewListener listener = getPrebidListener(adMobLoadListener);
        adView = new DisplayView(
                configuration.getContext(),
                listener,
                adConfiguration,
                response
        );
    }

    @NonNull
    private DisplayViewListener getPrebidListener(
            MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> adMobLoadListener
    ) {
        return new DisplayViewListener() {

            @Override
            public void onAdLoaded() {
                adMobBannerListener = adMobLoadListener.onSuccess(() -> adView);
            }

            @Override
            public void onAdDisplayed() {
                if (adMobBannerListener != null) {
                    adMobBannerListener.reportAdImpression();
                }
            }

            @Override
            public void onAdClicked() {
                if (adMobBannerListener != null) {
                    adMobBannerListener.onAdOpened();
                    adMobBannerListener.reportAdClicked();
                }
            }

            @Override
            public void onAdClosed() {
                if (adMobBannerListener != null) {
                    adMobBannerListener.onAdClosed();
                }
            }

            @Override
            public void onAdFailed(AdException exception) {
                adMobLoadListener.onFailure(AdErrors.failedToLoadAd(exception.getMessage()));
            }

        };
    }

}
