package com.elypia.alexis.entities;

import javax.persistence.*;

@Entity(name = "message_channel")
@Table
public class ChannelData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private long id;

    @Column(name = "guild_id")
    private long guildId;

    @Column(name = "channel_language")
    private String language;

    @Column(name = "clever_state")
    private String cleverState;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public String getLanguage() {
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
