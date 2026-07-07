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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.sellwild.prebid.SellwildPrebid;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class MraidEnvTest {

    @Test
    public void getWindowMraidEnv_ReturnProperlyFormedMraid() {
        String expectedValue = "window.MRAID_ENV = {"
                + "version: \"" + SellwildPrebid.MRAID_VERSION + "\","
                + "sdk: \"" + SellwildPrebid.SDK_NAME + "\","
                + "sdkVersion: \"" + SellwildPrebid.SDK_VERSION + "\","
                + "appId: \"null\","
                + "ifa: \"null\","
                + "limitAdTracking: false,"
                + "};";

        assertEquals(expectedValue, MraidEnv.getWindowMraidEnv());
    }
}
