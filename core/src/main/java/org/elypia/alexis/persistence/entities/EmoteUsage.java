/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.persistence.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Records when the emote was used and how many times.
 *
 * @author seth@elypia.org
 */
@Entity
@Table(name = "emote_usage")
public class EmoteUsage implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emote_usage_id")
    private int id;

    /**
     * The ID of the emote that was used.
     * It's possible this emote may not exist any longer.
     */
    @ManyToOne
    @JoinColumn(name = "emote_id", nullable = false)
    private EmoteData emoteData;

    /**
     * The guild these emotes were used in.
     * This can be any guild, not just the guild that owns the emote.
     */
    @ManyToOne
    @JoinColumn(name = "usage_guild_id", nullable = false)
    private GuildData usageGuildData;

    /** The number of times this emote occured in the message. */
    @Column(name = "occurences", nullable = false)
    private int occurences;

    /** The time the emote(s) were used. */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "usage_timestamp", nullable = false)
    private Date timestamp;

    public EmoteUsage() {
        // Do nothing
    }

    public EmoteUsage(EmoteData emoteData, GuildData usageGuildData, int occurences) {
        this(emoteData, usageGuildData, occurences, new Date());
    }

    public EmoteUsage(EmoteData emoteData, GuildData usageGuildData, int occurences, Date timestamp) {
        this.emoteData = emoteData;
        this.usageGuildData = usageGuildData;
        this.occurences = occurences;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        final String format = "%s(Occurences: %,d, Timestamp: %s)";
        return String.format(format, this.getClass(), occurences, timestamp);
    }

    public int getId() {
        return id;
    }

    public EmoteData getEmoteData() {
        return emoteData;
    }

    public void setEmoteData(EmoteData emoteData) {
        this.emoteData = emoteData;
    }

    public GuildData getUsageGuildData() {
        return usageGuildData;
    }

    public void setUsageGuildData(GuildData usageGuildData) {
        this.usageGuildData = usageGuildData;
    }

    public int getOccurences() {
        return occurences;
    }

    public void setOccurences(int occurences) {
        this.occurences = occurences;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
