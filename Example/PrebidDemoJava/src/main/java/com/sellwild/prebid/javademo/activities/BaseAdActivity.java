package com.sellwild.prebid.javademo.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sellwild.prebid.javademo.R;
import com.sellwild.prebid.javademo.databinding.ActivityDemoBinding;
import com.sellwild.prebid.javademo.testcases.TestCase;
import com.sellwild.prebid.javademo.testcases.TestCaseRepository;
import com.sellwild.prebid.javademo.utils.Settings;

public class BaseAdActivity extends AppCompatActivity {

    protected ActivityDemoBinding binding;

    private TestCase testCase = TestCaseRepository.lastTestCase;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_demo);
        binding.tvTestCaseName.setText(getString(testCase.getTitleStringRes()));
    }

    protected ViewGroup getAdWrapperView() {
        return binding.frameAdWrapper;
    }

    protected int getRefreshTimeSeconds() {
        return Settings.get().getRefreshTimeSeconds();
    }

}
