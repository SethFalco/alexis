package com.elypia.alexis.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("guilds")
public class GuildData {

    @Id
    private ObjectId id;

    /**
     * The long ID of the guild this instance holds data for.
     */

    @Property("guild_id")
    private long guildId;

    /**
     * The total amount of XP all members together have earned
     * while in this guild.
     */

    @Property("xp")
    private int xp;

    @Embedded("settings")
    private GuildSettings settings;

    public ObjectId getId() {
        return id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public GuildSettings getSettings() {
        return settings;
    }

    public void setSettings(GuildSettings settings) {
        this.settings = settings;
    }
}
