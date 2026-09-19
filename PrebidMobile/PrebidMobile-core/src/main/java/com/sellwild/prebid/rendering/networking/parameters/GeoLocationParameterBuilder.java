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

package com.sellwild.prebid.rendering.networking.parameters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.telephony.TelephonyManager;

import com.sellwild.prebid.LogUtil;
import com.sellwild.prebid.SellwildPrebid;
import com.sellwild.prebid.TargetingParams;
import com.sellwild.prebid.Util;
import com.sellwild.prebid.rendering.models.openrtb.bidRequests.geo.Geo;
import com.sellwild.prebid.rendering.sdk.ManagersResolver;
import com.sellwild.prebid.rendering.sdk.PrebidContextHolder;
import com.sellwild.prebid.rendering.sdk.deviceData.managers.DeviceInfoManager;
import com.sellwild.prebid.rendering.sdk.deviceData.managers.LocationInfoManager;

import java.util.List;
import java.util.Locale;

public class GeoLocationParameterBuilder extends ParameterBuilder {

    public static final int LOCATION_SOURCE_GPS = 1;

    @Override
    public void appendBuilderParameters(AdRequestInput adRequestInput) {
        LocationInfoManager locationInfoManager = ManagersResolver.getInstance().getLocationManager();
        DeviceInfoManager deviceManager = ManagersResolver.getInstance().getDeviceManager();

        // Strictly ignore publisher geo values
        adRequestInput.getBidRequest().getDevice().setGeo(null);

        if (locationInfoManager != null && SellwildPrebid.isShareGeoLocation()) {
            if (deviceManager != null && (deviceManager.isPermissionGranted("android.permission.ACCESS_FINE_LOCATION")
                    || deviceManager.isPermissionGranted("android.permission.ACCESS_COARSE_LOCATION"))) {
                setLocation(adRequestInput, locationInfoManager);
            }
        }
    }

    private void setLocation(AdRequestInput adRequestInput, LocationInfoManager locationInfoManager) {
        Double latitude = locationInfoManager.getLatitude();
        Double longitude = locationInfoManager.getLongitude();
        if (latitude == null || longitude == null) {
            locationInfoManager.resetLocation();
            latitude = locationInfoManager.getLatitude();
            longitude = locationInfoManager.getLongitude();
        }

        Geo geo = adRequestInput.getBidRequest().getDevice().getGeo();
        if (latitude != null && longitude != null) {
            Integer precision = TargetingParams.getLocationDecimalPrecision();
            geo.lat = Util.applyLocationPrecision(latitude.floatValue(), precision);
            geo.lon = Util.applyLocationPrecision(longitude.floatValue(), precision);
            geo.type = LOCATION_SOURCE_GPS;
            try {

                geo.country = getTelephonyCountry(PrebidContextHolder.getContext());

                if(geo.country.equals("")){
                    Locale locale = PrebidContextHolder.getContext().getResources().getConfiguration().locale;
                    geo.country = locale.getISO3Country();
                }

                if(geo.country.equals("")){
                    Geocoder geoCoder = new Geocoder(PrebidContextHolder.getContext());
                    List<Address> list = geoCoder.getFromLocation(locationInfoManager.getLatitude(), locationInfoManager.getLongitude(), 1);
                    geo.country = list.get(0).getCountryCode();
                }

                // Normalize to ISO-3166-1 alpha-3 (oRTB device.geo.country wants
                // alpha-3, e.g. "USA"). The telephony (getSimCountryIso /
                // getNetworkCountryIso) and Geocoder (Address.getCountryCode)
                // sources return alpha-2 (e.g. "US"); only the Locale fallback was
                // already alpha-3. Without this, alpha-2 leaks to the wire and
                // buyers mis-bucket geo. Idempotent for values already alpha-3.
                geo.country = toAlpha3(geo.country);

            }catch(Throwable thr){
                LogUtil.debug("Error getting country code");
            }
        }
    }

    /**
     * Convert an ISO-3166-1 alpha-2 country code to alpha-3 (e.g. "US" -> "USA",
     * "GB" -> "GBR") via the JDK's own ISO tables — full coverage, not just North
     * America. Returns a value already alpha-3 unchanged, and "" for empty /
     * unknown / unconvertible input (so the caller emits no country rather than a
     * malformed one).
     */
    static String toAlpha3(String country) {
        if (country == null) {
            return "";
        }
        String c = country.trim().toUpperCase(Locale.ROOT);
        if (c.length() == 3) {
            return c; // already alpha-3
        }
        if (c.length() != 2) {
            return "";
        }
        try {
            String iso3 = new Locale("", c).getISO3Country();
            return iso3 != null ? iso3 : "";
        } catch (Throwable thr) {
            return ""; // MissingResourceException for an unknown alpha-2
        }
    }

    private String getTelephonyCountry(Context ctx){
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        if(tm != null) {
            String simCountry = tm.getSimCountryIso().toUpperCase();
            String networkCountry = tm.getNetworkCountryIso().toUpperCase();

            if (!simCountry.equals("")) {
                return simCountry;
            } else if (!networkCountry.equals("")) {
                return networkCountry;
            }
        }
        return "";
    }
}
