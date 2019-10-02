/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.utils;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public final class YouTubeUtils {

    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final String THUMBNAIL_FORMAT = "http://img.youtube.com/vi/%s/maxresdefault.jpg";

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

            if (started)
                joiner.add(String.format("%02d", hours));
            else
                joiner.add(String.valueOf(hours));

            started = true;
        }

        long minutes = TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS);
        seconds -= TimeUnit.SECONDS.convert(minutes, TimeUnit.MINUTES);

        if (started)
            joiner.add(String.format("%02d", minutes));
        else
            joiner.add(String.valueOf(minutes));

        joiner.add(String.format("%02d", seconds));
        return joiner.toString();
    }

    /**
     * Forms the YouTube video url from an identifier provided. <br>
     * Do note: Does not use an API call.
     * Simple prepends standard youtube video url.
     *
     * @param id The identifier of the video.
     * @return Url to the video id provided.
     */

    public static String getVideoUrl(String id) {
        return VIDEO_URL + id;
    }

    /**
     * Forms the thumbnail url from an identifier provided. <br>
     * Do note: Does not use an API call.
     * Simply inserts id into standard thumbnail url.
     *
     * @param id The identifier of the video.
     * @return Url to the high quality youtube thumbnail.
     */

    public static String getThumbnailUrl(String id) {
        return String.format(THUMBNAIL_FORMAT, id);
    }
}
