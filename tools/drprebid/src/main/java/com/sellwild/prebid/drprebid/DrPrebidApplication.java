package com.sellwild.prebid.drprebid;

import androidx.multidex.MultiDexApplication;
import com.sellwild.prebid.ServerRequestSettings;
import com.sellwild.prebid.drprebid.managers.LineItemKeywordManager;

public class DrPrebidApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ServerRequestSettings.update(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LineItemKeywordManager.getInstance().refreshCacheIds(this);
    }
}
