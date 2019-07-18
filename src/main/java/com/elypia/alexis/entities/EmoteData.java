package com.elypia.alexis.entities;

import javax.persistence.*;

@Entity(name = "emote")
@Table
public class EmoteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emote_id")
    private long id;

    @Column(name = "guild_id")
    private long guildId;

    public long getId() {
        return id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }
}
