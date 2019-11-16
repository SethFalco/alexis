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

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YouTubeUtilsTest {

    @Test
    public void testTimestampHmmss() {
        String expected = "1:43:36";
        String actual = YouTubeUtils.toYouTubeTimeFormat(TimeUnit.SECONDS, 6216);
        assertEquals(expected, actual);
    }

    @Test
    public void testTimestampmmss() {
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
