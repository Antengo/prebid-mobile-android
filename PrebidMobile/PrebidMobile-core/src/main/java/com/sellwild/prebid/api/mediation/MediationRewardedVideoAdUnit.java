/*
 *    Copyright 2018-2021 Prebid.org, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.sellwild.prebid.api.mediation;

import android.content.Context;

import androidx.annotation.NonNull;

import com.sellwild.prebid.AdSize;
import com.sellwild.prebid.LogUtil;
import com.sellwild.prebid.api.data.AdFormat;
import com.sellwild.prebid.api.data.FetchDemandResult;
import com.sellwild.prebid.api.mediation.listeners.OnFetchCompleteListener;
import com.sellwild.prebid.rendering.bidding.data.bid.BidResponse;
import com.sellwild.prebid.rendering.bidding.display.BidResponseCache;
import com.sellwild.prebid.rendering.bidding.display.PrebidMediationDelegate;
import com.sellwild.prebid.rendering.models.AdPosition;

/**
 * Mediation rewarded ad unit for Rendering API with AdMob or AppLovin MAX.
 */
public class MediationRewardedVideoAdUnit extends MediationBaseFullScreenAdUnit {

    private static final String TAG = "MediationRewardedAdUnit";

    /**
     * Default constructor.
     */
    public MediationRewardedVideoAdUnit(
        Context context,
        String configId,
        PrebidMediationDelegate mediationDelegate
    ) {
        super(context, configId, null, mediationDelegate);
    }

    /**
     * Loads ad and applies mediation delegate.
     *
     * @param listener callback when operation is completed (success or fail)
     */
    public void fetchDemand(@NonNull OnFetchCompleteListener listener) {
        super.fetchDemand(listener);
    }

    @Override
    protected final void initAdConfig(
        String configId,
        AdSize adSize
    ) {
        adUnitConfig.setConfigId(configId);
        adUnitConfig.setAdFormat(AdFormat.VAST);
        adUnitConfig.setRewarded(true);
        adUnitConfig.setAdPosition(AdPosition.FULLSCREEN);
    }

    @Override
    protected final void onResponseReceived(BidResponse response) {
        if (onFetchCompleteListener != null) {
            LogUtil.debug(TAG, "On response received");
            BidResponseCache.getInstance().putBidResponse(response.getId(), response);
            mediationDelegate.setResponseToLocalExtras(response);
            mediationDelegate.handleKeywordsUpdate(response.getTargeting());
            onFetchCompleteListener.onComplete(FetchDemandResult.SUCCESS);
        }
    }

}
