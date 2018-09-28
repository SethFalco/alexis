package com.elypia.alexis.entities;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.entities.embedded.TranslateSettings;
import com.elypia.alexis.entities.impl.DatabaseEntity;
import com.elypia.elypiai.utils.Language;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.query.Query;

@Entity(value = "message_channels", noClassnameStored = true)
public class MessageChannelData implements DatabaseEntity {

    @Id
    private ObjectId id;

    @Property("channel_id")
    private long channelId;

    @Property("language")
    private String language;

    @Property("clever_state")
    private String cleverState;

    public MessageChannelData() {

    }

    public MessageChannelData(long channelId) {
        this.channelId = channelId;
    }

    public static MessageChannelData query(long channelId) {
        var data = Alexis.getDatabaseManager().query(MessageChannelData.class, "channel_id", channelId);

        if (data == null)
            data = new MessageChannelData(channelId);

        return data;
    }

    public static MessageChannelData query(String field, String value) {
        return Alexis.getDatabaseManager().query(MessageChannelData.class, field, value);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getLanguage() {
        if (language == null)
            return Language.ENGLISH.getCode();

        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCleverState() {
        return cleverState;
    }

    public void setCleverState(String cleverState) {
        this.cleverState = cleverState;
    }
}
