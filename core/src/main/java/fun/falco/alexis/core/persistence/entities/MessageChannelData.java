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
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@Entity
@Table(name = "message_channel")
public class MessageChannelData implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "channel_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private GuildData guildData;

    @Column(name = "channel_locale")
    private Locale locale;

    @Column(name = "cleverbot_state", length = 8196)
    private String cleverState;

    public MessageChannelData() {
        // Do nothing
    }

    public MessageChannelData(long channelId, GuildData guildData) {
        this();
        this.id = channelId;
        this.guildData = guildData;
    }

    public long getId() {
        return id;
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public void setGuildData(GuildData guildData) {
        this.guildData = guildData;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getCleverState() {
        return cleverState;
    }

    public void setCleverState(String cleverState) {
        this.cleverState = cleverState;
    }
}
