package com.elypia.alexis.entities.embedded;

import xyz.morphia.annotations.*;

@Embedded
public class LevelSettings {

    @Property("sync_to_global")
    private boolean syncToGlobal;

    @Embedded("message")
    private MessageSettings notifySettings;

    public boolean isSyncToGlobal() {
        return syncToGlobal;
    }

    public void setSyncToGlobal(boolean syncToGlobal) {
        this.syncToGlobal = syncToGlobal;
    }

    public MessageSettings getNotifySettings() {
        if (notifySettings == null)
            notifySettings = new MessageSettings();

        return notifySettings;
    }

    public void setNotifySettings(MessageSettings notifySettings) {
        this.notifySettings = notifySettings;
    }
}
