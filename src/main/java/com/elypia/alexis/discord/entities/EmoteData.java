package com.elypia.alexis.discord.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.Date;
import java.util.List;

@Entity("emote")
public class EmoteData {

    @Id
    private ObjectId id;

    @Property("emote_id")
    private long emoteId;

    @Property("guild_id")
    private long guildId;

    @Property("uses")
    private List<Date> uses;

    public int incrementUseCount(int value) {
        for (int i = 0; i < value; i++)
            uses.add(new Date());

        return uses.size();
    }

    public ObjectId getId() {
        return id;
    }

    public long getEmoteId() {
        return emoteId;
    }

    public void setEmoteId(long emoteId) {
        this.emoteId = emoteId;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public List<Date> getUses() {
        return uses;
    }

    public void setUses(List<Date> uses) {
        this.uses = uses;
    }
}
