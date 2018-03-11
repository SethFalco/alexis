package com.elypia.alexis.discord.entities.data;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * Tags are key words that can be associated with {@link Guild}
 * {@link TextChannel}'s and allow customising how Alexis acts
 * depending on the channel. <br>
 * Tags which are associated with moderation such as Advertising
 * will be overridden if someone with at least the
 * {@link Permission#MANAGE_SERVER} sends the message.
 */

public enum Tag {

    /**
     * Prevent non-admins from sending things like Discord guild
     * invite links or bot invite links.
     */

    ADVERTISING("advertising", TagFilter.ONLY),

    /**
     * Set it so Alexis can ignore any toggled channels.
     */

    IGNORE("ignore", TagFilter.ONLY),

    /**
     * Affects the international (request translations
     * via reacting with the respective flag) functionality will be available.
     */

    INTERNATIONAL("international", TagFilter.ONLY),

    /**
     * Limit which channels instances of minigames can be started.
     */

    MINIGAME("minigane", TagFilter.ONLY),

    /**
     * Restrict which channels music commands and notifications
     * can actually be posted in.
     */

    MUSIC("music", TagFilter.ONLY),

    /**
     * Affects if users will be able to gain XP in this channel.
     */

    SPAM("spam", TagFilter.ONLY);

    /**
     *  The name of the tag as it appears in the database.
     */

    private String databaseName;

    /**
     * The default filter value of the tag. <br>
     * <strong>{@link TagFilter#ONLY}</strong> means only the listed channels.
     * <strong>{@link TagFilter#EXCEPT}</strong> means all but the listed channels.
     */

    private TagFilter defaultFilter;

    Tag(String databaseName, TagFilter defaultFilter) {
        this.databaseName = databaseName;
        this.defaultFilter = defaultFilter;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public TagFilter getDefaultFilter() {
        return defaultFilter;
    }

    public static Tag getByName(String databaseName) {
        return valueOf(databaseName.toUpperCase());
    }
}
