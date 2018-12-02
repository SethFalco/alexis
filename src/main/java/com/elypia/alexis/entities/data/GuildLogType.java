package com.elypia.alexis.entities.data;

/**
 * These are the reasons we might log to a guilds specified log_channel
 * if one is specified, this allows them to essentially subscribe or
 * unsubscribe from specific events.
 */
public enum GuildLogType {

    /**
     * This refers to whenever the bot performs an action
     * in the guild such as applying a role, or
     * renaming a user.
     */
    GUILD_ACTION("Guild Action"),

    /**
     * This refers to whenever the bot performs an action
     * which involves the guild but doesn't directly affect it.
     * For example a user earning an achievement or leveling up.
     */
    INTERNAL_ACTION("Internal Action"),

    /**
     * This refers to when a user may perform a command at limited
     * functionality or do something wrong that could cause some
     * kind of negative consequence.
     */
    WARNING("Warning"),

    /**
     * This refers to when a developer wants to annouce something
     * to all guilds accepting annoucements. This could be a new feature
     * or a problem.
     */
    ANNOUCEMENT("Annoucement"),

    /**
     * This refers to technical difficulties, for example
     * if there is problems with the database or a module goes down.
     */
    TECHINCAL_ISSUE("Technical Issue");

    private final String NAME;

    GuildLogType(String name) {
        NAME = name;
    }

    public String getName() {
        return NAME;
    }
}
