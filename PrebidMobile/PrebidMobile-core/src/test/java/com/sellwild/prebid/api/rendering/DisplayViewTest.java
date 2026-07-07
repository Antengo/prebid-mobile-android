package com.sellwild.prebid.api.rendering;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.sellwild.prebid.api.rendering.pluginrenderer.SellwildPrebidPluginRegister.PREBID_MOBILE_RENDERER_NAME;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import com.sellwild.prebid.SellwildPrebid;
import com.sellwild.prebid.api.data.AdFormat;
import com.sellwild.prebid.api.rendering.pluginrenderer.SellwildPrebidPluginRenderer;
import com.sellwild.prebid.configuration.AdUnitConfiguration;
import com.sellwild.prebid.rendering.bidding.data.bid.Bid;
import com.sellwild.prebid.rendering.bidding.data.bid.BidResponse;
import com.sellwild.prebid.rendering.bidding.listeners.DisplayVideoListener;
import com.sellwild.prebid.rendering.bidding.listeners.DisplayViewListener;
import com.sellwild.prebid.testutils.FakeSellwildPrebidPluginRenderer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class DisplayViewTest {

    private DisplayView displayView;
    private Context context;
    private BidResponse mockResponse;
    private SellwildPrebidPluginRenderer fakeSellwildPrebidPluginRenderer;
    @Spy
    private AdUnitConfiguration adUnitConfiguration;
    @Mock
    private View mockBannerView;
    @Mock
    private DisplayViewListener mockDisplayViewListener;
    @Mock
    private DisplayVideoListener mockDisplayVideoListener;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(DisplayViewTest.this);

        context = Robolectric.buildActivity(Activity.class).create().get();

        adUnitConfiguration.setAdFormat(AdFormat.BANNER);

        fakeSellwildPrebidPluginRenderer = Mockito.spy(FakeSellwildPrebidPluginRenderer.getFakePrebidRenderer(null, mockBannerView, true, PREBID_MOBILE_RENDERER_NAME, "1.0"));
        SellwildPrebid.registerPluginRenderer(fakeSellwildPrebidPluginRenderer);

        mockResponse = mock(BidResponse.class);
        Bid mockBid = mock(Bid.class);
        when(mockBid.getAdm()).thenReturn("adm");
        when(mockResponse.getWinningBid()).thenReturn(mockBid);
        when(mockResponse.getPreferredPluginRendererName()).thenReturn(PREBID_MOBILE_RENDERER_NAME);
    }

    @Test
    public void onDisplayViewWinNotification_returnBannerAdView() {
        displayView = new DisplayView(context, mockDisplayViewListener, adUnitConfiguration, mockResponse);


        verify(adUnitConfiguration).modifyUsingBidResponse(mockResponse);
        verify(fakeSellwildPrebidPluginRenderer).createBannerAdView(context, mockDisplayViewListener, null, adUnitConfiguration, mockResponse);
        // bannerAdView added to view hierarchy
        assertEquals(1, displayView.getChildCount());
    }

    @Test
    public void onDisplayViewWithVideoListenerWinNotification_returnBannerAdView() {
        displayView = new DisplayView(context, mockDisplayViewListener, mockDisplayVideoListener, adUnitConfiguration, mockResponse);

        verify(adUnitConfiguration).modifyUsingBidResponse(mockResponse);
        verify(fakeSellwildPrebidPluginRenderer).createBannerAdView(context, mockDisplayViewListener, mockDisplayVideoListener, adUnitConfiguration, mockResponse);
        // bannerAdView added to view hierarchy
        assertEquals(1, displayView.getChildCount());
    }
}