/*
 *    Copyright 2018-2019 Prebid.org, Inc.
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

package com.sellwild.prebid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static com.sellwild.prebid.api.rendering.pluginrenderer.SellwildPrebidPluginRegister.PREBID_MOBILE_RENDERER_NAME;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.sellwild.prebid.api.rendering.pluginrenderer.SellwildPrebidPluginRenderer;
import com.sellwild.prebid.configuration.PBSConfig;
import com.sellwild.prebid.reflection.Reflection;
import com.sellwild.prebid.reflection.sdk.SellwildPrebidReflection;
import com.sellwild.prebid.testutils.BaseSetup;
import com.sellwild.prebid.testutils.FakeSellwildPrebidPluginRenderer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = BaseSetup.testSDK)
public class SellwildPrebidTest extends BaseSetup {

    @Before
    public void clean() {
        Reflection.setStaticVariableTo(SellwildPrebid.class, "customStatusEndpoint", null);
        Reflection.setStaticVariableTo(SellwildPrebid.class, "auctionSettingsId", null);
    }


    @Test
    public void testSellwildPrebidSettings() {
        SellwildPrebid.setPrebidServerAccountId("123456");
        assertEquals("123456", SellwildPrebid.getPrebidServerAccountId());
        SellwildPrebid.setTimeoutMillis(2500);
        assertEquals(2500, SellwildPrebid.getTimeoutMillis());
        assertNotNull(activity.getApplicationContext());
        SellwildPrebid.initializeSdk(activity.getApplicationContext(), "https://test.com", null);
        SellwildPrebid.setShareGeoLocation(true);
        assertTrue(SellwildPrebid.isShareGeoLocation());
        assertEquals(Host.createCustomHost("https://test.com"), SellwildPrebid.getPrebidServerHost());
        SellwildPrebid.setStoredAuctionResponse("111122223333");
        assertEquals("111122223333", SellwildPrebid.getStoredAuctionResponse());
        SellwildPrebid.addStoredBidResponse("appnexus", "221144");
        SellwildPrebid.addStoredBidResponse("rubicon", "221155");
        assertFalse(SellwildPrebid.getStoredBidResponses().isEmpty());
        SellwildPrebid.clearStoredBidResponses();
        assertTrue(SellwildPrebid.getStoredBidResponses().isEmpty());
        SellwildPrebid.setPbsDebug(true);
        assertTrue(SellwildPrebid.getPbsDebug());
        SellwildPrebid.setCreativeFactoryTimeout(7000);
        assertEquals(7000, SellwildPrebid.getCreativeFactoryTimeout());
        SellwildPrebid.setCreativeFactoryTimeoutPreRenderContent(25000);
        assertEquals(25000, SellwildPrebid.getCreativeFactoryTimeoutPreRenderContent());
        SellwildPrebid.setAuctionSettingsId("987654");
        assertEquals("987654", SellwildPrebid.getAuctionSettingsId());
    }

    @Test
    public void testSetCustomHeaders() {
        HashMap<String, String> customHeaders = new HashMap<>();
        customHeaders.put("key1", "value1");
        customHeaders.put("key2", "value2");
        SellwildPrebid.setCustomHeaders(customHeaders);

        assertFalse(SellwildPrebid.getCustomHeaders().isEmpty());
        assertEquals(2, SellwildPrebid.getCustomHeaders().size());
    }

    @Test
    public void setCustomStatusEndpoint_nullValue() {
        SellwildPrebid.setCustomStatusEndpoint(null);

        assertNull(getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_ipAddress() {
        SellwildPrebid.setCustomStatusEndpoint("192.168.0.106");

        assertEquals("https://192.168.0.106/", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_valueWithoutHttp() {
        SellwildPrebid.setCustomStatusEndpoint("site.com");

        assertEquals("https://site.com/", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_valueWithoutHttpWithThreeW() {
        SellwildPrebid.setCustomStatusEndpoint("www.site.com");

        assertEquals("https://www.site.com/", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_valueWithoutHttpWithThreeWAndPath() {
        SellwildPrebid.setCustomStatusEndpoint("www.site.com/status");

        assertEquals("https://www.site.com/status", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_goodValue() {
        SellwildPrebid.setCustomStatusEndpoint("http://site.com/status");

        assertEquals("http://site.com/status", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_goodValueSecure() {
        SellwildPrebid.setCustomStatusEndpoint("https://site.com/status?test=1");

        assertEquals("https://site.com/status?test=1", getInnerCustomEndpointValue());
    }

    private String getInnerCustomEndpointValue() {
        return SellwildPrebidReflection.getCustomStatusEndpoint();
    }

    @Test
    public void registerPluginRenderer_registerProperly() {
        // Given
        SellwildPrebidPluginRenderer fakeSellwildPrebidPluginRenderer = FakeSellwildPrebidPluginRenderer.getFakePrebidRenderer(
                null,
                null,
                true,
                PREBID_MOBILE_RENDERER_NAME,
                "1.0"
        );

        // When
        SellwildPrebid.registerPluginRenderer(fakeSellwildPrebidPluginRenderer);

        // Then
        assertTrue(SellwildPrebid.containsPluginRenderer(fakeSellwildPrebidPluginRenderer));
    }

    @Test
    public void registerPluginRenderer_unregisterProperly() {
        // Given
        SellwildPrebidPluginRenderer fakeSellwildPrebidPluginRenderer = FakeSellwildPrebidPluginRenderer.getFakePrebidRenderer(
                null,
                null,
                true,
                PREBID_MOBILE_RENDERER_NAME,
                "1.0"
        );

        // When
        SellwildPrebid.registerPluginRenderer(fakeSellwildPrebidPluginRenderer);

        // Then
        assertTrue(SellwildPrebid.containsPluginRenderer(fakeSellwildPrebidPluginRenderer));

        // When
        SellwildPrebid.unregisterPluginRenderer(fakeSellwildPrebidPluginRenderer);

        // Then
        assertFalse(SellwildPrebid.containsPluginRenderer(fakeSellwildPrebidPluginRenderer));
    }

    @Test
    public void getCreativeFactoryTimeouts_usePbsConfig() {
        SellwildPrebid.setPbsConfig(new PBSConfig(9000, 20000));
        SellwildPrebid.setCreativeFactoryTimeout(8000);
        SellwildPrebid.setCreativeFactoryTimeoutPreRenderContent(21000);
        assertEquals(9000, SellwildPrebid.getCreativeFactoryTimeout());
        assertEquals(20000, SellwildPrebid.getCreativeFactoryTimeoutPreRenderContent());
    }

    @Test
    public void getCreativeFactoryTimeouts_useSdk() {
        SellwildPrebid.setCreativeFactoryTimeout(8000);
        SellwildPrebid.setCreativeFactoryTimeoutPreRenderContent(21000);
        assertEquals(8000, SellwildPrebid.getCreativeFactoryTimeout());
        assertEquals(21000, SellwildPrebid.getCreativeFactoryTimeoutPreRenderContent());
    }

}
