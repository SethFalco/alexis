package com.elypia.alexis.entities.embedded;

import org.mongodb.morphia.annotations.*;

@Embedded
public class TranslateSettings {

    /**
     * Is the translate on reaction functionality
     * enabled for this guild.
     */
    @Property("enabled")
    private boolean enabled;

    /**
     * When the reaction is performed, are we allowed to
     * post responses in the Guild at all or should we
     * always use the DMs of the user.
     */
    @Property("private")
    private boolean inPrivate;

    public TranslateSettings() {

    }

    public TranslateSettings(boolean enabled, boolean inPrivate) {
        this.enabled = enabled;
        this.inPrivate = inPrivate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPrivate() {
        return inPrivate;
    }

    public void setPrivate(boolean inPrivate) {
        this.inPrivate = inPrivate;
    }
}
