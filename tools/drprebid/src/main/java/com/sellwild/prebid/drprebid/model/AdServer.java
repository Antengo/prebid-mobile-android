package com.sellwild.prebid.drprebid.model;

import com.sellwild.prebid.drprebid.Constants;

public enum AdServer {
    GOOGLE_AD_MANAGER(Constants.Settings.AdServerCodes.GOOGLE_AD_MANAGER);

    private int code;

    AdServer(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
