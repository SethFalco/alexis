/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fun.falco.alexis.core.modules.youtube;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class YouTubeUtilsTest {

    @Test
    public void testTimestampHmmss() {
        String expected = "1:43:36";
        String actual = YouTubeUtils.toYouTubeTimeFormat(TimeUnit.SECONDS, 6216);
        assertEquals(expected, actual);
    }

    @Test
    public void testTimestampMmss() {
        String expected = "44:36";
        String actual = YouTubeUtils.toYouTubeTimeFormat(TimeUnit.SECONDS, 2676);
        assertEquals(expected, actual);
    }

    @Test
    public void testDurationAndTimestampHmmss() {
        String expected = "3:08 / 1:43:36";
        String actual = YouTubeUtils.toYouTubeTimeFormat(TimeUnit.SECONDS, 188, 6216);
        assertEquals(expected, actual);
    }
}
