package com.elypia.alexis.entities.embedded;

import xyz.morphia.annotations.*;

@Embedded
public class GuildLogSettings {

    @Property("enabled")
    private boolean enabled;

    @Property("log_channel")
    private long logChannel;

    public GuildLogSettings() {

    }

    public GuildLogSettings(boolean enabled, long logChannel) {
        this.enabled = enabled;
        this.logChannel = logChannel;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getLogChannel() {
        return logChannel;
    }

    public void setLogChannel(long logChannel) {
        this.logChannel = logChannel;
    }
}
