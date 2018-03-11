package com.elypia.alexis.discord.entities.data;

public enum Tag {

    /**
     * Affects if users will be able to gain XP in this channel.
     */

    SPAM("spam"),

    /**
     * Affects the international (request translations
     * via reacting with the respective flag) functionality will be available.
     *
     */

    INTERNATIONAL("international");

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
