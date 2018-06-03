package com.elypia.alexis.entities;

import org.mongodb.morphia.annotations.*;

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
        return message;
    }

    public void setMessages(String message) {
        this.message = message;
    }
}
