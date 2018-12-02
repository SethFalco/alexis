package com.elypia.alexis.entities;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.embedded.GuildSettings;
import com.elypia.alexis.entities.impl.*;
import net.dv8tion.jda.core.entities.Guild;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity(value = "guilds", noClassnameStored = true)
public class GuildData extends Experienceable implements DatabaseEntity {

    @Id
    private ObjectId id;

    /**
     * The long ID of the guild this instance holds data for.
     */
    @Property("guild_id")
    private long guildId;

    @Embedded("settings")
    private GuildSettings settings;

    public GuildData() {

    }

    public GuildData(long guildId) {
        this.guildId = guildId;
    }

    public static GuildData query(Guild guild) {
        return query(guild.getIdLong());
    }

    public static GuildData query(long guildId) {
        GuildData data = query("guild_id", guildId);

        if (data == null)
            return new GuildData(guildId);

        return data;
    }

    public static <T> GuildData query(String field, T value) {
        return Alexis.getDatabaseManager().query(GuildData.class, field, value);
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

    public GuildSettings getSettings() {
        if (settings == null)
            settings = new GuildSettings();

        return settings;
    }

    public void setSettings(GuildSettings settings) {
        this.settings = settings;
    }
}
