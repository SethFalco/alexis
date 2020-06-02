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

import javax.persistence.*;
import java.io.Serializable;

/**
 * Allows guilds to choose what messages they are subscribed too.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Entity
@Table(name = "role")
public class RoleData implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "role_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildData guildData;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public void setGuildData(GuildData guildData) {
        this.guildData = guildData;
    }
}
