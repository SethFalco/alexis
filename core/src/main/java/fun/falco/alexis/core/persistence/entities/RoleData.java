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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Allows guilds to choose what messages they are subscribed too.
 *
 * @author seth@falco.fun (Seth Falco)
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

    @Column(name = "self_assignable")
    private boolean selfAssignable;

    /** Should this role be applied to users automatically on join. */
    @Column(name = "on_user_join")
    private boolean onUserJoin;

    /** Should this role be applied to bots automatically on join. */
    @Column(name = "on_bot_join")
    private boolean onBotJoin;

    public RoleData() {
        // Do nothing
    }

    public RoleData(long roleId) {
        this.id = roleId;
    }

    public RoleData(long roleId, GuildData guildData) {
        this(roleId);
        this.guildData = guildData;
    }

    public RoleData(long roleId, GuildData guildData, boolean selfAssignable) {
        this(roleId, guildData);
        this.selfAssignable = selfAssignable;
    }

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

    public boolean isSelfAssignable() {
        return selfAssignable;
    }

    public void setSelfAssignable(boolean selfAssignable) {
        this.selfAssignable = selfAssignable;
    }

    public boolean isOnUserJoin() {
        return onUserJoin;
    }

    public void setOnUserJoin(boolean onUserJoin) {
        this.onUserJoin = onUserJoin;
    }

    public boolean isOnBotJoin() {
        return onBotJoin;
    }

    public void setOnBotJoin(boolean onBotJoin) {
        this.onBotJoin = onBotJoin;
    }
}
