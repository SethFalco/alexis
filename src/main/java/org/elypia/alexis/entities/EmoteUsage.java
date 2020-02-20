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

package org.elypia.alexis.entities;

import net.dv8tion.jda.api.entities.Guild;

import javax.enterprise.inject.Vetoed;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Records when the emote was used and how many times.
 *
 * @author seth@elypia.org
 */
@Entity(name = "emote_usage")
@Table
@Vetoed
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
    @Column(name = "emote_id")
    private Long emoteId;

    /** The {@link Guild} these emotes were used in. */
    @Column(name = "guild_id")
    private Long guildId;

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
