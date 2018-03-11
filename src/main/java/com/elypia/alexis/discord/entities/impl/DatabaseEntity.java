package com.elypia.alexis.discord.entities.impl;

/**
 * This interface is <strong>ONLY</strong> for objects that
 * wrap around a collection as a whole, and should not be
 * be inheritted for smaller objects that are a part of a larger one.
 * For exmaple, guilds, and users. However not for tags or textchannels.
 */

public interface DatabaseEntity {

    /**
     * Commit any changes that have been made so far
     * to the database.
     */

    void commit();
}
