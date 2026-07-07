package com.sellwild.prebid.reflection.sdk;

import android.content.Context;

import org.mockito.Mockito;
import com.sellwild.prebid.Host;
import com.sellwild.prebid.SellwildPrebid;
import com.sellwild.prebid.reflection.Reflection;
import com.sellwild.prebid.rendering.sdk.InitializationNotifier;
import com.sellwild.prebid.rendering.sdk.PrebidContextHolder;

public class SellwildPrebidReflection {

    public static void setHost(String host) {
        Reflection.setStaticVariableTo(SellwildPrebid.class, "host", Host.createCustomHost(host));
    }

    public static void setCustomStatusEndpoint(String url) {
        Reflection.setStaticVariableTo(SellwildPrebid.class, "customStatusEndpoint", url);
    }

    public static String getCustomStatusEndpoint() {
        return Reflection.getStaticFieldOf(SellwildPrebid.class, "customStatusEndpoint");
    }

    public static void setFlagsThatSdkIsInitialized() {
        Reflection.setStaticVariableTo(InitializationNotifier.class, "tasksCompletedSuccessfully", true);
        Reflection.setStaticVariableTo(InitializationNotifier.class, "initializationInProgress", false);
        PrebidContextHolder.setContext(Mockito.mock(Context.class));
    }

    public static void setFlagsThatSdkIsNotInitialized() {
        Reflection.setStaticVariableTo(InitializationNotifier.class, "tasksCompletedSuccessfully", false);
        Reflection.setStaticVariableTo(InitializationNotifier.class, "initializationInProgress", false);
        PrebidContextHolder.clearContext();
    }

    public static void setDisableStatusCheckToTrue() {
        Reflection.setStaticVariableTo(SellwildPrebid.class, "disableStatusCheck", true);
    }
}
