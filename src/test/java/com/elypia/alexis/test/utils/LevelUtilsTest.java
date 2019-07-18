package com.elypia.alexis.test.utils;

import com.elypia.alexis.utils.LevelUtils;
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
