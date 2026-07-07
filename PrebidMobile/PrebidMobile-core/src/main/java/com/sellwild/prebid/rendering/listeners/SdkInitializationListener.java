package com.sellwild.prebid.rendering.listeners;

import org.jetbrains.annotations.NotNull;
import com.sellwild.prebid.api.data.InitializationStatus;

public interface SdkInitializationListener {

    void onInitializationComplete(@NotNull InitializationStatus status);

}
