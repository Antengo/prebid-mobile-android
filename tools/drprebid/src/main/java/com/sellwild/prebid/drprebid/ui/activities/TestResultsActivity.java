package com.sellwild.prebid.drprebid.ui.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.MobileAds;
import com.sellwild.prebid.drprebid.R;
import com.sellwild.prebid.drprebid.managers.SettingsManager;
import com.sellwild.prebid.drprebid.model.AdServerSettings;
import com.sellwild.prebid.drprebid.ui.adapters.TestResultsAdapter;
import com.sellwild.prebid.drprebid.ui.viewmodels.AdServerValidationViewModel;
import com.sellwild.prebid.drprebid.ui.viewmodels.PrebidServerValidationViewModel;
import com.sellwild.prebid.drprebid.ui.viewmodels.SdkValidationViewModel;
import com.sellwild.prebid.drprebid.validation.AdServerTest;
import com.sellwild.prebid.drprebid.validation.RealTimeDemandTest;
import com.sellwild.prebid.drprebid.validation.SdkTest;

public class TestResultsActivity extends AppCompatActivity {

    private RecyclerView listView;

    private AdServerValidationViewModel adServerValidationViewModel;
    private PrebidServerValidationViewModel demandValidationViewModel;
    private SdkValidationViewModel sdkValidationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);

        listView = findViewById(R.id.list_results);
        setupResultsList();

        adServerValidationViewModel = ViewModelProviders.of(this).get(AdServerValidationViewModel.class);
        demandValidationViewModel = ViewModelProviders.of(this).get(PrebidServerValidationViewModel.class);
        sdkValidationViewModel = ViewModelProviders.of(this).get(SdkValidationViewModel.class);

        AdServerSettings adServerSettings = SettingsManager.getInstance(this).getAdServerSettings();

        initGoogleAdsManager();

    }



    private void initGoogleAdsManager() {
        MobileAds.initialize(this.getApplication());
        runTests();
    }

    private void setupResultsList() {
        TestResultsAdapter adapter = new TestResultsAdapter();

        listView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
    }

    private void runTests() {
        runAdServerValidationTest();
    }

    private void runAdServerValidationTest() {
        AdServerTest adServerTest = new AdServerTest(this, new AdServerTest.Listener() {
            @Override
            public void onPrebidKeywordsFoundOnRequest() {
                adServerValidationViewModel.setRequestSent(true);
            }

            @Override
            public void onPrebidKeywordsNotFoundOnRequest() {
                adServerValidationViewModel.setRequestSent(false);
            }

            @Override
            public void adServerResponseContainsPrebidCreative(@Nullable Boolean contains) {
                adServerValidationViewModel.setCreativeServed(contains);
            }

            @Override
            public void onTestFinished() {
                runDemandValidationTest();
            }
        });

        adServerTest.startTest();
    }

    private void runDemandValidationTest() {
        RealTimeDemandTest demandValidator = new RealTimeDemandTest(this, results -> {
            int totalBids = results.getTotalBids();

            demandValidationViewModel.setBidResponseReceivedCount(totalBids);
            demandValidationViewModel.setAverageCpm(results.getAvgEcpm());
            demandValidationViewModel.setAverageResponseTime(results.getAvgResponseTime());

            runSdkValidationTest();
        });

        demandValidator.startTest();
        demandValidationViewModel.setBidRequestsSent(true);
    }

    private void runSdkValidationTest() {
        SdkTest sdkTest = new SdkTest(this, new SdkTest.Listener() {
            @Override
            public void onAdUnitRegistered() {
                sdkValidationViewModel.setAdUnitRegistered(true);
            }

            @Override
            public void requestToPrebidServerSent(boolean sent) {
                sdkValidationViewModel.setPrebidRequestSent(sent);
            }

            @Override
            public void responseFromPrebidServerReceived(boolean received) {
                sdkValidationViewModel.setPrebidResponseReceived(received);
            }

            @Override
            public void bidReceivedAndCached(boolean received) {
                sdkValidationViewModel.setCreativeContentCached(received);
            }

            @Override
            public void requestSentToAdServer(String request, String postBody) {
                sdkValidationViewModel.setAdServerRequestSent(true);
            }

            @Override
            public void adServerResponseContainsPrebidCreative(@Nullable Boolean contains) {
                sdkValidationViewModel.setCreativeServed(contains);
            }

            @Override
            public void onTestFinished() {

            }
        });

        sdkTest.startTest();
    }
}
