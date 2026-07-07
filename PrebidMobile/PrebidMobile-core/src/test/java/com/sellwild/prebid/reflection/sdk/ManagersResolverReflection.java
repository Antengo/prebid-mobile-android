package com.sellwild.prebid.reflection.sdk;

import com.sellwild.prebid.reflection.Reflection;
import com.sellwild.prebid.rendering.sdk.ManagersResolver;

public class ManagersResolverReflection {

    public static void resetManagers(ManagersResolver resolver) {
        Reflection.setVariableTo(resolver, "deviceManager", null);
        Reflection.setVariableTo(resolver, "locationManager", null);
        Reflection.setVariableTo(resolver, "connectionManager", null);
        Reflection.setVariableTo(resolver, "userConsentManager", null);
    }

}
