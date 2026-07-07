package com.sellwild.prebid.drprebid.model;

import com.sellwild.prebid.drprebid.Constants;

public enum AdFormat {
    BANNER(Constants.Settings.AdFormatCodes.BANNER),
    INTERSTITIAL(Constants.Settings.AdFormatCodes.INTERSTITIAL);

    private int code;

    AdFormat(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
