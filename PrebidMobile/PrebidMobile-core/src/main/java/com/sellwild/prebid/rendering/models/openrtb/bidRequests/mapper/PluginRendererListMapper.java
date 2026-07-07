package com.sellwild.prebid.rendering.models.openrtb.bidRequests.mapper;

import com.sellwild.prebid.api.rendering.pluginrenderer.SellwildPrebidPluginRenderer;
import com.sellwild.prebid.rendering.models.openrtb.bidRequests.PluginRenderer;

import java.util.ArrayList;
import java.util.List;

public class PluginRendererListMapper {

    public List<PluginRenderer> map(List<SellwildPrebidPluginRenderer> prebidMobilePluginRenderers) {
        List<PluginRenderer> renderers = new ArrayList<>();
        for(SellwildPrebidPluginRenderer element: prebidMobilePluginRenderers){
            PluginRenderer pluginRenderer = map(element);
            renderers.add(pluginRenderer);
        }
        return renderers;
    }

    private PluginRenderer map(SellwildPrebidPluginRenderer prebidMobilePluginRenderer) {
        PluginRenderer pluginRenderer = new PluginRenderer();
        pluginRenderer.setName(prebidMobilePluginRenderer.getName());
        pluginRenderer.setVersion(prebidMobilePluginRenderer.getVersion());
        pluginRenderer.setData(prebidMobilePluginRenderer.getData());
        return pluginRenderer;
    }
}
