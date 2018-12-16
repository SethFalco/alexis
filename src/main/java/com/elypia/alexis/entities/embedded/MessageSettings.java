package com.elypia.alexis.entities.embedded;

import xyz.morphia.annotations.*;

@Embedded
public class MessageSettings {

    @Property("enabled")
    private boolean enabled;

    @Property("channel")
    private long channel;

    @Property("message")
    private String message;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getChannel() {
        return channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }

    public String getMessage() {
        if (message == null)
            return "Congratulations, ($user.mention) just advanced a ($user.role) level.\nYour ($user.role) level is now ($user.level).";

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
