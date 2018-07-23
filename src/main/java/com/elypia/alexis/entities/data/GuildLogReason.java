package com.elypia.alexis.entities.data;

public enum GuildLogReason {

    /**
     * This may occur whenever we update a member,
     * for example their role, nickname or permissions.
     */

    MEMBER_UDPATE("Member Update", GuildLogType.GUILD_ACTION),

    /**
     * This may occur whenever we update a channel,
     * for example renaming it.
     */

    CHANNEL_UPDATE("Channel Update", GuildLogType.GUILD_ACTION),

    /**
     * This may occur whenever a user levels up in the guild,
     * or globally if level sync is enabled.
     */

    MEMBER_LEVEL("Member Level", GuildLogType.ACTION),

    /**
     * This may occur whenever a user earns a global achievement.
     */

    USER_ACHIEVEMENT("User Achievement", GuildLogType.ACTION),

    /**
     * This may occur whenever the connection to the database changes,
     * for example if we lose connection or manage to reconnect.
     */

    DATABASE_STATUS("Database Status", GuildLogType.TECHINCAL_ISSUE),

    /**
     * This warning displays when a guild doesn't hasn't granted
     * the permission to post embeds in a particular channel and
     * we've had to use a fallback.
     */

    EMBED_PERMISSION("Embed Permission", GuildLogType.WARNING),

    /**
     * This warning displays when a guild hasn't granted
     * the permissiont to add reactions in a particular channel
     * and we're unable to perform reaction functionality.
     */

    REACT_PERMISSION("React Permission", GuildLogType.WARNING);

    private final String NAME;
    private final GuildLogType TYPE;

    GuildLogReason(String name, GuildLogType type) {
        NAME = name;
        TYPE = type;
    }

    public String getNAME() {
        return NAME;
    }

    public GuildLogType getTYPE() {
        return TYPE;
    }
}
