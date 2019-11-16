/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.entities;

import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.*;
import java.util.Date;

/**
 * Records when the emote was used and how many times.
 *
 * @author seth@elypia.org
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
