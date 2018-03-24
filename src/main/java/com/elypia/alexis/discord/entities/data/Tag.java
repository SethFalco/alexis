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
 * {@link Permission#MANAGE_SERVER} permission sends the message.
 */

public enum Tag {

    /**
     * Prevent non-admins from sending things like Discord guild
     * invite links or bot invite links.
     */

    ADVERTISING("advertising"),

    /**
     * Set it so Alexis can ignore any toggled channels.
     */

    IGNORE("ignore"),

    /**
     * Affects which channels the international (request translations
     * via reacting with the respective flag) functionality will be available.
     */

    INTERNATIONAL("international"),

    /**
     * Have Alexis entirely block or redirect media from non-media channels
     * to channels marked with the media tag.
     */

    MEDIA("media"),

    /**
     * Limit which channels instances of minigames can be started.
     */

    MINIGAME("minigane"),

    /**
     * Restrict which channels music commands and notifications
     * can actually be posted in.
     */

    MUSIC("music"),

    /**
     * Affects if users will be able to gain XP in this channel.
     */

    SPAM("spam");

    /**
     *  The name of the tag as it appears in the database.
     */

    private String databaseName;

    Tag(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public static Tag getByName(String databaseName) {
        return valueOf(databaseName.toUpperCase());
    }
}
