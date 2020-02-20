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

/**
 * Utilities regarding the leveling system used by Elypia.
 * This should be standardised across applications but
 * is currently coded direcly in Alexis only.
 *
 * @author seth@elypia.org (Seth Falco)
 */
public final class LevelUtils {

    private static final float ONE_OVER_THREE = 1f / 3f;

    private LevelUtils() {
        // Do nothing
    }

    /**
     * @param level The level to get the minimum XP of.
     * @return The minimum amount of XP required to be at this level.
     */
    public static long getXpFromLevel(int level) {
        return getXpFromLevel(level, 1);
    }

    /**
     * @param level The level to get the minimum XP of.
     * @param multipler The multiplier to apply to the XP. (A higher multipler requires more XP per level.)
     * @return The minimum amount of XP required to be at this level.
     */
    public static long getXpFromLevel(int level, float multipler) {
        return (long)((64 * Math.pow(level - 1d, 3)) * multipler);
    }

    /**
     * @param xp The amount of XP to convert to a level.
     * @return The level an entity would be with this much XP.
     */
    public static int getLevelFromXp(long xp) {
        return getLevelFromXp(xp, 1);
    }

    /**
     * @param xp The amount of XP to convert to a level.
     * @return The level an entity would be with this much XP.
     */
    public static int getLevelFromXp(long xp, float mulitplier) {
        return (int)Math.pow((xp / mulitplier) / 64, ONE_OVER_THREE) + 1;
    }
}
