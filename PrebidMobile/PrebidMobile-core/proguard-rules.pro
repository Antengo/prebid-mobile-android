# Save names for all Prebid classes
-keepnames class com.sellwild.prebid.**
-keepnames interface com.sellwild.prebid.**
-keepnames enum com.sellwild.prebid.**

# Google Ad Manager and AdMob
-keep class com.sellwild.prebid.PrebidNativeAd { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdView { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdRequest { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdRequest$Builder { *; }
-keep interface com.google.android.gms.ads.nativead.NativeCustomFormatAd { *; }
-keep interface com.google.android.gms.ads.formats.NativeCustomTemplateAd { *; }