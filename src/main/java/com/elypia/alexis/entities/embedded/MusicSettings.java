package com.elypia.alexis.entities.embedded;

import org.mongodb.morphia.annotations.*;

@Embedded
public class MusicSettings {

    @Property("nickname_sync")
    private boolean syncNickname;

    public boolean getSyncNickname() {
        return syncNickname;
    }

    public void setSyncNickname(boolean syncNickname) {
        this.syncNickname = syncNickname;
    }
}
