/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.core.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fun.falco.alexis.core.persistence.enums.GuildMessageType;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@Entity
@Table(
    name = "guild_message",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"guild_id", "message_type"})
    })
public class GuildMessage implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private GuildData guildData;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private GuildMessageType type;

    /** Optional, null means current channel. */
    @Column(name = "message_channel_id")
    private Long channelId;

    @Column(name = "message")
    private String message;

    public GuildMessage() {
        // Do nothing
    }

    public GuildMessage(GuildData guildData, GuildMessageType type, Long channelId) {
        this.guildData = guildData;
        this.type = type;
        this.channelId = channelId;
    }

    public GuildMessage(GuildData guildData, GuildMessageType type, Long channelId, String body) {
        this(guildData, type, channelId);
        this.message = body;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GuildMessage)) {
            return false;
        }

        GuildMessage gm = (GuildMessage) o;

        return id == gm.id &&
            guildData.equals(gm.guildData) &&
            type == gm.type &&
            channelId.equals(gm.channelId) &&
            message.equals(gm.message);
    }

    public int getId() {
        return id;
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public void setGuildData(GuildData guildData) {
        this.guildData = guildData;
    }

    public GuildMessageType getType() {
        return type;
    }

    public void setType(GuildMessageType type) {
        this.type = type;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
