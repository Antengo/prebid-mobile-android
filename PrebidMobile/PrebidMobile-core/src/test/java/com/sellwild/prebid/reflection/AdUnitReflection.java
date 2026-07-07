package com.sellwild.prebid.reflection;

import com.sellwild.prebid.AdUnit;
import com.sellwild.prebid.rendering.bidding.loader.BidLoader;

public class AdUnitReflection {

    public static void setBidLoader(AdUnit adUnit, BidLoader bidLoader) {
        Reflection.setVariableTo(adUnit, "bidLoader", bidLoader);
    }

}
