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

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * Provides static utilities based on YouTube.
 * This does not actually connect to or use the YouTube API,
 * it only contains logic which is based on or derived from YouTube.
 *
 * @author seth@falco.fun (Seth Falco)
 */
public final class YouTubeUtils {

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";

    private static final String CHANNEL_URL = "https://www.youtube.com/channel/";

    private YouTubeUtils() {
        // Do nothing
    }

    public static String toYouTubeTimeFormat(TimeUnit unit, long current, long duration) {
        return String.format("%s / %s", toYouTubeTimeFormat(unit, current), toYouTubeTimeFormat(unit, duration));
    }

    public static String toYouTubeTimeFormat(TimeUnit unit, long time) {
        long seconds = TimeUnit.SECONDS.convert(time, unit);
        StringJoiner joiner = new StringJoiner(":");
        boolean started = false;

        long days = TimeUnit.DAYS.convert(seconds, TimeUnit.SECONDS);
        if (days != 0) {
            seconds -= TimeUnit.SECONDS.convert(days, TimeUnit.DAYS);
            joiner.add(String.valueOf(days));
            started = true;
        }

        long hours = TimeUnit.HOURS.convert(seconds, TimeUnit.SECONDS);
        if (hours != 0 || started) {
            seconds -= TimeUnit.SECONDS.convert(hours, TimeUnit.HOURS);

            if (started) {
                joiner.add(String.format("%02d", hours));
            } else {
                joiner.add(String.valueOf(hours));
            }

            started = true;
        }

        long minutes = TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS);
        seconds -= TimeUnit.SECONDS.convert(minutes, TimeUnit.MINUTES);

        if (started) {
            joiner.add(String.format("%02d", minutes));
        } else {
            joiner.add(String.valueOf(minutes));
        }

        joiner.add(String.format("%02d", seconds));
        return joiner.toString();
    }

    /**
     * Forms the YouTube video url from an identifier provided. <br>
     * Do note: Does not use an API call.
     * Simple prepends standard youtube video url.
     *
     * @param id Identifier of the video.
     * @return URL to the video with ID.
     */
    public static String getVideoUrl(String id) {
        return VIDEO_URL + id;
    }

    public static String getChannelUrl(String channelId) {
        return CHANNEL_URL + channelId;
    }
}
