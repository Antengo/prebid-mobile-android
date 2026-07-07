package com.sellwild.prebid.api.original;

import androidx.annotation.NonNull;

import com.sellwild.prebid.api.data.BidInfo;

/**
 * Fetch demand listener for original API {@link PrebidAdUnit}.
 */
public interface OnFetchDemandResult {

    void onComplete(@NonNull BidInfo bidInfo);

}
