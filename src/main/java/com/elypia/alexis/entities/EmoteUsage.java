package com.elypia.alexis.entities;

import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.*;
import java.util.Date;

/**
 * Records when the emote was used and how many times.
 */
@Entity(name = "emote_usage")
@Table
public class EmoteUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emote_usage_id")
    private int id;

    /**
     * The ID of the emote that was used.
     * It's possible this emote may not exist any longer.
     */
    @Column(name = "emote_id")
    private long emoteId;

    /** The {@link Guild} these emotes were used in. */
    @Column(name = "guild_id")
    private long guildId;

    /** The number of times this emote occured in the message. */
    @Column(name = "occurences")
    private int count;

    /** The time the emote(s) were used. */
    @Column(name = "usage_timestamp")
    private Date timestamp;

    public EmoteUsage() {
        // Do nothing
    }

    public EmoteUsage(long emoteId, long guildId, int count) {
        this(emoteId, guildId, count, new Date());
    }

    public EmoteUsage(long emoteId, long guildId, int count, Date timestamp) {
        this.emoteId = emoteId;
        this.guildId = guildId;
        this.count = count;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public long getEmoteId() {
        return emoteId;
    }

    public void setEmoteId(long emoteId) {
        this.emoteId = emoteId;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
