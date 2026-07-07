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

package com.sellwild.prebid.api.rendering.pluginrenderer;

import androidx.annotation.VisibleForTesting;

import com.sellwild.prebid.LogUtil;
import com.sellwild.prebid.configuration.AdUnitConfiguration;
import com.sellwild.prebid.rendering.bidding.data.bid.BidResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plugin registerer.
 */
public class SellwildPrebidPluginRegister {

    public static final String PREBID_MOBILE_RENDERER_NAME = "PrebidRenderer";
    private static final String TAG = SellwildPrebidPluginRegister.class.getSimpleName();

    private static final SellwildPrebidPluginRegister instance = new SellwildPrebidPluginRegister();

    private final HashMap<String, SellwildPrebidPluginRenderer> plugins = new HashMap<>();

    public void registerPlugin(SellwildPrebidPluginRenderer prebidMobilePluginRenderers) {
        String rendererName = prebidMobilePluginRenderers.getName();
        if (plugins.containsKey(rendererName)) {
            LogUtil.debug(TAG, "New plugin renderer with name" + rendererName + "will replace the previous one with same name");
        }
        plugins.put(prebidMobilePluginRenderers.getName(), prebidMobilePluginRenderers);
    }

    public void unregisterPlugin(SellwildPrebidPluginRenderer prebidMobilePluginRenderer) {
        plugins.remove(prebidMobilePluginRenderer.getName());
    }

    public Boolean containsPlugin(SellwildPrebidPluginRenderer prebidMobilePluginRenderer) {
        return plugins.containsKey(prebidMobilePluginRenderer.getName());
    }

    @VisibleForTesting
    public Boolean containsPlugin(String prebidMobilePluginRendererName) {
        return plugins.containsKey(prebidMobilePluginRendererName);
    }

    public void registerEventListener(
            PluginEventListener pluginEventListener,
            String listenerKey
    ) {
        if (plugins.containsKey(pluginEventListener.getPluginRendererName())) {
            plugins.get(pluginEventListener.getPluginRendererName()).registerEventListener(pluginEventListener, listenerKey);
        } else {
            LogUtil.debug(TAG, "Skipping PluginEventListener with name" + pluginEventListener.getPluginRendererName() + ", such key does not exist");
        }
    }

    public void unregisterEventListener(String listenerKey) {
        for (Map.Entry<String, SellwildPrebidPluginRenderer> entry : plugins.entrySet()) {
            SellwildPrebidPluginRenderer renderer = entry.getValue();
            renderer.unregisterEventListener(listenerKey);
        }
    }

    // Returns the list of available renderers for the given ad unit for RT request
    public List<SellwildPrebidPluginRenderer> getRTBListOfRenderersFor(AdUnitConfiguration adUnitConfiguration) {
        List<SellwildPrebidPluginRenderer> compliantPlugins = new ArrayList<>();
        for (Map.Entry<String, SellwildPrebidPluginRenderer> entry : plugins.entrySet()) {
            SellwildPrebidPluginRenderer renderer = entry.getValue();
            if (renderer.isSupportRenderingFor(adUnitConfiguration)) {
                compliantPlugins.add(renderer);
            }
        }
        return compliantPlugins;
    }

    // Returns the registered renderer according to the preferred renderer data in the bid response
    // If no preferred renderer is found, it returns PrebidRenderer to perform default behavior
    public SellwildPrebidPluginRenderer getPluginForPreferredRenderer(BidResponse bidResponse) {
        String preferredRendererName = bidResponse.getPreferredPluginRendererName();
        String preferredRendererVersion = bidResponse.getPreferredPluginRendererVersion();
        SellwildPrebidPluginRenderer preferredPlugin = plugins.get(preferredRendererName);
        if (preferredPlugin != null
                && preferredPlugin.isSupportRenderingFor(bidResponse.getAdUnitConfiguration())
                && preferredPlugin.getVersion().equals(preferredRendererVersion)) {
            return preferredPlugin;
        } else {
            return plugins.get(PREBID_MOBILE_RENDERER_NAME);
        }
    }

    private SellwildPrebidPluginRegister() {
    }

    public static SellwildPrebidPluginRegister getInstance() {
        return instance;
    }
}
