package com.elypia.alexis.utils;

/**
 * Utilities regarding the leveling system used by Elypia.
 * This should be standardised across applications but
 * is currently coded direcly in ChatBot only.
 */
public final class LevelUtils {

    private static final float ONE_OVER_THREE = 1 / 3;

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
        return (long)((64 * Math.pow(level - 1, 3)) * multipler);
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
