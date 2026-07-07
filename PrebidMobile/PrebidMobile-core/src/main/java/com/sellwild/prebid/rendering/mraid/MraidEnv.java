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

package com.sellwild.prebid.rendering.mraid;

import androidx.annotation.NonNull;
import com.sellwild.prebid.SellwildPrebid;
import com.sellwild.prebid.rendering.sdk.ManagersResolver;
import com.sellwild.prebid.rendering.utils.helpers.AdvertisingIdManager;
import com.sellwild.prebid.rendering.utils.helpers.AppInfoManager;

public class MraidEnv {

    private MraidEnv() {

    }

    @NonNull
    public static String getWindowMraidEnv() {
        return "window.MRAID_ENV = {"
                + getStringPropertyWithSeparator("version", SellwildPrebid.MRAID_VERSION)
                + getStringPropertyWithSeparator("sdk", SellwildPrebid.SDK_NAME)
                + getStringPropertyWithSeparator("sdkVersion", SellwildPrebid.SDK_VERSION)
                + getStringPropertyWithSeparator("appId", AppInfoManager.getPackageName())
                + getStringPropertyWithSeparator("ifa", AdvertisingIdManager.getAdvertisingId(ManagersResolver.getInstance().getUserConsentManager()))
                + getBooleanPropertyWithSeparator("limitAdTracking", AdvertisingIdManager.isLimitedAdTrackingEnabled(), ",")
                + "};";
    }

    static String getStringPropertyWithSeparator(String propertyName, String propertyValue) {
        String separator = ",";
        return String.format("%s: \"%s\"%s", propertyName, propertyValue, separator);
    }

    static String getBooleanPropertyWithSeparator(String propertyName, boolean propertyValue, String separator) {
        return String.format("%s: %s%s", propertyName, propertyValue, separator);
    }
}
