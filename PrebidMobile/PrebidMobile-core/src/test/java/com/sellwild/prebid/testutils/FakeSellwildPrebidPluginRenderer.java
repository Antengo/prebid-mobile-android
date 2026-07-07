package com.sellwild.prebid.testutils;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;
import com.sellwild.prebid.api.rendering.SellwildPrebidInterstitialControllerInterface;
import com.sellwild.prebid.api.rendering.pluginrenderer.PluginEventListener;
import com.sellwild.prebid.api.rendering.pluginrenderer.SellwildPrebidPluginRenderer;
import com.sellwild.prebid.configuration.AdUnitConfiguration;
import com.sellwild.prebid.rendering.bidding.data.bid.BidResponse;
import com.sellwild.prebid.rendering.bidding.display.InterstitialController;
import com.sellwild.prebid.rendering.bidding.interfaces.InterstitialControllerListener;
import com.sellwild.prebid.rendering.bidding.listeners.DisplayVideoListener;
import com.sellwild.prebid.rendering.bidding.listeners.DisplayViewListener;

public class FakeSellwildPrebidPluginRenderer {
    public static SellwildPrebidPluginRenderer getFakePrebidRenderer(
            InterstitialController mockInterstitialController,
            View mockBannerAdView,
            Boolean isSupportRenderingFor,
            String rendererName,
            String rendererVersion
    ) {
        return new SellwildPrebidPluginRenderer() {
            @Override
            public String getName() { return rendererName; }

            @Override
            public String getVersion() { return rendererVersion; }

            @Nullable
            @Override
            public JSONObject getData() { return null; }

            @Override
            public void registerEventListener(PluginEventListener pluginEventListener, String listenerKey) { }

            @Override
            public void unregisterEventListener(String listenerKey) { }

            @Override
            public View createBannerAdView(
                    @NonNull Context context,
                    @NonNull DisplayViewListener displayViewListener,
                    @Nullable DisplayVideoListener displayVideoListener,
                    @NonNull AdUnitConfiguration adUnitConfiguration,
                    @NonNull BidResponse bidResponse
            ) {
                return mockBannerAdView;
            }

            @Override
            public SellwildPrebidInterstitialControllerInterface createInterstitialController(
                    @NonNull Context context,
                    @NonNull InterstitialControllerListener interstitialControllerListener,
                    @NonNull AdUnitConfiguration adUnitConfiguration,
                    @NonNull BidResponse bidResponse
            ) {
                return mockInterstitialController;
            }

            @Override
            public boolean isSupportRenderingFor(AdUnitConfiguration adUnitConfiguration) {
                return isSupportRenderingFor;
            }
        };
    }
}
