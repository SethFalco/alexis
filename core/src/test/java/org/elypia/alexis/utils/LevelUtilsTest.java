/*
 * Copyright 2019-2020 Elypia CIC
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
