package com.sellwild.prebid.renderingtestapp.plugplay.bidding.ppm

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.sellwild.prebid.AdSize
import com.sellwild.prebid.api.exceptions.AdException
import com.sellwild.prebid.api.rendering.BannerView
import com.sellwild.prebid.api.rendering.listeners.BannerViewListener
import com.sellwild.prebid.renderingtestapp.AdFragment
import com.sellwild.prebid.renderingtestapp.R
import com.sellwild.prebid.renderingtestapp.databinding.FragmentBiddingBannerReusableBinding
import com.sellwild.prebid.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.sellwild.prebid.renderingtestapp.utils.BaseEvents

open class PpmBannerReusableFragment : AdFragment() {

    override val layoutRes = R.layout.fragment_bidding_banner_reusable

    protected var bannerView: BannerView? = null

    protected val binding: FragmentBiddingBannerReusableBinding
        get() = getBinding()

    protected val events by lazy { Events(binding.root) }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)

        binding.adIdLabel.text = getString(R.string.label_auid, configId)
        binding.btnLoad.setOnClickListener {
            resetEventButtons()
            bannerView?.loadAd()
        }
        binding.btnRemoveFromContainer.isEnabled = false
        binding.btnAddToContainer.setOnClickListener {
            binding.viewContainer.addView(bannerView, dpToPx(320f), dpToPx(50f))
            binding.btnAddToContainer.isEnabled = false
            binding.btnRemoveFromContainer.isEnabled = true
        }
        binding.btnRemoveFromContainer.setOnClickListener {
            binding.viewContainer.removeAllViews()
            binding.btnAddToContainer.isEnabled = true
            binding.btnRemoveFromContainer.isEnabled = false
        }
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    override fun initAd(): Any? {
        bannerView = BannerView(requireContext(), configId, AdSize(width, height))
        bannerView?.setAutoRefreshDelay(refreshDelay)
        bannerView?.setBannerListener(createListener())
        return bannerView
    }

    override fun loadAd() {}

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode? {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerView?.destroy()
    }

    private fun createListener(): BannerViewListener {
        return object : BannerViewListener {
            override fun onAdLoaded(bannerView: BannerView?) {
                resetEventButtons()
                events.loaded(true)
            }

            override fun onAdDisplayed(bannerView: BannerView?) {
                events.displayed(true)
            }

            override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {
                resetEventButtons()
                events.failed(true)
            }

            override fun onAdClicked(bannerView: BannerView?) {}

            override fun onAdClosed(bannerView: BannerView?) {}
        }
    }

    protected class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun displayed(b: Boolean) = enable(R.id.btnAdDisplayed, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

    }

}