package com.elypia.alexis.entities;

import javax.persistence.*;

@Entity(name = "message")
@Table
public class MessageSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int id;

    @Column(name = "message_type")
    private int type;

    @Column(name = "message_enabled")
    private boolean enabled;

    /**
     * Optional, null means current channel.
     */
    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "message")
    private String message;

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
