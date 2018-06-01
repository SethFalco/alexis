package com.elypia.alexis.discord.entities;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

@Embedded
public class LevelSettings {

    @Property("sync_to_global")
    private boolean syncToGlobal;

    @Reference("message")
    private MessageSettings notifySettings;

    public boolean isSyncToGlobal() {
        return syncToGlobal;
    }

    public void setSyncToGlobal(boolean syncToGlobal) {
        this.syncToGlobal = syncToGlobal;
    }

    public MessageSettings getNotifySettings() {
        return notifySettings;
    }

    public void setNotifySettings(MessageSettings notifySettings) {
        this.notifySettings = notifySettings;
    }
}
