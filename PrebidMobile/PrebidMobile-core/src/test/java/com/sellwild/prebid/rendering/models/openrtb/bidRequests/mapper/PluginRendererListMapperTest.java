package com.sellwild.prebid.rendering.models.openrtb.bidRequests.mapper;

import static org.junit.Assert.*;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import com.sellwild.prebid.api.rendering.SellwildPrebidInterstitialControllerInterface;
import com.sellwild.prebid.api.rendering.pluginrenderer.PluginEventListener;
import com.sellwild.prebid.api.rendering.pluginrenderer.SellwildPrebidPluginRenderer;
import com.sellwild.prebid.configuration.AdUnitConfiguration;
import com.sellwild.prebid.rendering.bidding.data.bid.BidResponse;
import com.sellwild.prebid.rendering.bidding.interfaces.InterstitialControllerListener;
import com.sellwild.prebid.rendering.bidding.listeners.DisplayVideoListener;
import com.sellwild.prebid.rendering.bidding.listeners.DisplayViewListener;
import com.sellwild.prebid.rendering.models.openrtb.bidRequests.PluginRenderer;

import java.util.ArrayList;
import java.util.List;

public class PluginRendererListMapperTest {

    String pluginName;
    String pluginVersion;
    JSONObject pluginData;

    SellwildPrebidPluginRenderer testPlugin = new SellwildPrebidPluginRenderer() {
        @Override
        public String getName() {
            return pluginName;
        }

        @Override
        public String getVersion() {
            return pluginVersion;
        }

        @Nullable
        @Override
        public JSONObject getData() {
            return pluginData;
        }

        @Override
        public void registerEventListener(PluginEventListener pluginEventListener, String listenerKey) { }

        @Override
        public void unregisterEventListener(String listenerKey) { }

        @Override
        public View createBannerAdView(@NonNull Context context, @NonNull DisplayViewListener displayViewListener, @Nullable DisplayVideoListener displayVideoListener, @NonNull AdUnitConfiguration adUnitConfiguration, @NonNull BidResponse bidResponse) {
            return null;
        }

        @Override
        public SellwildPrebidInterstitialControllerInterface createInterstitialController(@NonNull Context context, @NonNull InterstitialControllerListener interstitialControllerListener, @NonNull AdUnitConfiguration adUnitConfiguration, @NonNull BidResponse bidResponse) {
            return null;
        }

        @Override
        public boolean isSupportRenderingFor(AdUnitConfiguration adUnitConfiguration) {
            return false;
        }
    };

    @Test
    public void whenMap_valuesAreCorrect() throws JSONException {
        // Given
        PluginRendererListMapper mapper = new PluginRendererListMapper();
        List<SellwildPrebidPluginRenderer> pluginList = new ArrayList<>();
        pluginList.add(testPlugin);
        pluginName = "name";
        pluginVersion = "1.0";
        pluginData = new JSONObject();
        pluginData.put("key", true);

        // When
        List<PluginRenderer> result = mapper.map(pluginList);
        PluginRenderer mappedRenderer = result.get(0);

        // Then
        assertEquals(mappedRenderer.getName(), pluginName);
        assertEquals(mappedRenderer.getVersion(), pluginVersion);
        assertEquals(mappedRenderer.getData(), pluginData);
    }
}