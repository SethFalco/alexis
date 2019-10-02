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

import static org.junit.jupiter.api.Assertions.*;

public class LevelUtilsTest {

    @Test
    public void testXpFromLevel() {
        assertAll("Ensure all levels produce the correct amount of XP.",
            () -> assertEquals(0, LevelUtils.getXpFromLevel(1)),
            () -> assertEquals(46656, LevelUtils.getXpFromLevel(10)),
            () -> assertEquals(60236288, LevelUtils.getXpFromLevel(99))
        );
    }

    @Test
    public void testLevelFromXp() {
        assertAll("Ensure all XP values produced are correct against the level.",
            () -> assertEquals(1, LevelUtils.getLevelFromXp(0)),
            () -> assertEquals(10, LevelUtils.getLevelFromXp(46656)),
            () -> assertEquals(99, LevelUtils.getLevelFromXp(60236288))
        );
    }
}
