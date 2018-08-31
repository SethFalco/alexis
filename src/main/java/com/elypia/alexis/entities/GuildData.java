package com.elypia.alexis.entities;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.embedded.GuildSettings;
import com.elypia.alexis.entities.impl.DatabaseEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.query.Query;

@Entity(value = "guilds", noClassnameStored = true)
public class GuildData extends DatabaseEntity {

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

    public static GuildData query(long guildId) {
        Query<GuildData> query = Alexis.store.createQuery(GuildData.class);
        GuildData data = query.filter("guild_id ==", guildId).get();

        if (data == null) {
            data = new GuildData();
            data.guildId = guildId;
        }

        return data;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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
        if (settings == null)
            settings = new GuildSettings();

        return settings;
    }

    public void setSettings(GuildSettings settings) {
        this.settings = settings;
    }
}
