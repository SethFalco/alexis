package com.elypia.alexis.entities;

import com.elypia.alexis.entities.impl.DatabaseEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.*;

@Entity(value = "emote", noClassnameStored = true)
public class EmoteData extends DatabaseEntity {

    @Id
    private ObjectId id;

    @Property("emote_id")
    private long emoteId;

    @Property("guild_id")
    private long guildId;

// ! Turn this into an embedded array with date -> number of uses
    @Property("uses")
    private List<Date> uses;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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
