package com.elypia.alexis.entities.embedded;

import org.mongodb.morphia.annotations.*;

@Embedded
public class MusicSettings {

    @Property("nickname_sync")
    private boolean syncChannelName;

    public boolean getSyncChannelName() {
        return syncChannelName;
    }

    public void setSyncChannelName(boolean syncChannelName) {
        this.syncChannelName = syncChannelName;
    }
}
