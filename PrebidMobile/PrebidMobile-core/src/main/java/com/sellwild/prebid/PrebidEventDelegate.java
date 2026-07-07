package com.sellwild.prebid;

import org.json.JSONObject;

public interface PrebidEventDelegate {

    void onBidResponse(JSONObject request, JSONObject response);

}
