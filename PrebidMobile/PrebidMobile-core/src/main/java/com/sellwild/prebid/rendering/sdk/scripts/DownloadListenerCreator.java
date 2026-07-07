package com.sellwild.prebid.rendering.sdk.scripts;

import com.sellwild.prebid.rendering.loading.FileDownloadListener;

public interface DownloadListenerCreator {

    FileDownloadListener create(String filePath);

}