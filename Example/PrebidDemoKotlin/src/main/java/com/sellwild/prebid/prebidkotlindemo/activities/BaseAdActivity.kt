package com.sellwild.prebid.prebidkotlindemo.activities

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sellwild.prebid.prebidkotlindemo.R
import com.sellwild.prebid.prebidkotlindemo.databinding.ActivityDemoBinding
import com.sellwild.prebid.prebidkotlindemo.testcases.TestCase
import com.sellwild.prebid.prebidkotlindemo.testcases.TestCaseRepository
import com.sellwild.prebid.prebidkotlindemo.utils.Settings

open class BaseAdActivity : AppCompatActivity() {

    protected val TAG = "ExampleActivity"

    /**
     * ViewGroup container for any ad view.
     */
    protected val adWrapperView: ViewGroup
        get() = binding.frameAdWrapper

    /**
     * Seconds for auto-refreshing any banner ad.
     */
    protected val refreshTimeSeconds: Int
        get() = Settings.get().refreshTimeSeconds

    private lateinit var binding: ActivityDemoBinding
    private var testCase: TestCase = TestCaseRepository.lastTestCase

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_demo)
        binding.tvTestCaseName.text = getText(testCase.titleStringRes)
    }

}