package com.sellwild.prebid.admob;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdView;

import com.sellwild.prebid.LogUtil;
import com.sellwild.prebid.rendering.bidding.data.bid.BidResponse;
import com.sellwild.prebid.rendering.bidding.display.PrebidMediationDelegate;
import com.sellwild.prebid.rendering.models.internal.VisibilityTrackerOption;
import com.sellwild.prebid.rendering.models.ntv.NativeEventTracker;
import com.sellwild.prebid.rendering.utils.helpers.VisibilityChecker;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Banner mediation delegate.
 */
public class AdMobMediationBannerUtils implements PrebidMediationDelegate {

    private static final String TAG = "BannerMediationUtils";

    private final Bundle extras;
    private final WeakReference<AdView> adView;

    public AdMobMediationBannerUtils(
        Bundle adMobExtrasBundle,
        AdView adView
    ) {
        this.extras = adMobExtrasBundle;
        this.adView = new WeakReference<>(adView);
    }

    @Override
    public void setResponseToLocalExtras(@Nullable BidResponse response) {
        if (response != null) {
            extras.putString(PrebidBannerAdapter.EXTRA_RESPONSE_ID, response.getId());
        }
    }

    @Override
    public boolean canPerformRefresh() {
        AdView view = adView.get();
        if (view == null) {
            LogUtil.error(TAG, "AdView is null, it can be destroyed as WeakReference");
            return false;
        }

        final VisibilityTrackerOption visibilityTrackerOption = new VisibilityTrackerOption(NativeEventTracker.EventType.IMPRESSION);
        final VisibilityChecker checker = new VisibilityChecker(visibilityTrackerOption);

        boolean isVisible = checker.isVisibleForRefresh(view);
        if (isVisible) {
            Log.d(TAG, "Visibility checker result: " + true);
        } else {
            Log.e(TAG, "Can't perform refresh. Ad view is not visible.");
        }

        return isVisible;
    }

    @Override
    public void handleKeywordsUpdate(@Nullable HashMap<String, String> keywords) {

    }

}