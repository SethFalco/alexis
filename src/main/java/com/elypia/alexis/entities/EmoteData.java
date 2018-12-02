package com.elypia.alexis.entities;

import com.elypia.alexis.entities.embedded.EmoteEntry;
import com.elypia.alexis.entities.impl.DatabaseEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.List;

@Entity(value = "emotes", noClassnameStored = true)
public class EmoteData implements DatabaseEntity {

    @Id
    private ObjectId id;

    @Property("emote_id")
    private long emoteId;

    @Property("guild_id")
    private long guildId;

    @Embedded("uses")
    private List<EmoteEntry> entry;

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

    public List<EmoteEntry> getUses() {
        return entry;
    }

    public void setUses(List<EmoteEntry> entry) {
        this.entry = entry;
    }
}
