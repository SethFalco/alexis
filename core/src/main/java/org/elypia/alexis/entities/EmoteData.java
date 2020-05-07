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

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Entity
@Table(name = "emote")
public class EmoteData implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "emote_id")
    private long id;

    @Column(name = "guild_id")
    private long guildId;

    public EmoteData() {
        // Do nothing
    }

    public EmoteData(final long id, final long guildId) {
        this.id = id;
        this.guildId = guildId;
    }

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
