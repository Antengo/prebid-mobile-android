package com.sellwild.prebid.drprebid.model;

import com.sellwild.prebid.drprebid.Constants;

public enum PrebidServer {
    RUBICON(Constants.Settings.PrebidServerCodes.RUBICON),
    APPNEXUS(Constants.Settings.PrebidServerCodes.APPNEXUS),
    CUSTOM(Constants.Settings.PrebidServerCodes.CUSTOM);

    private int code;

    PrebidServer(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
