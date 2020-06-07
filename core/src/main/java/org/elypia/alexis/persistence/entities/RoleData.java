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
import java.util.*;

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

    @Column(name = "self_assignable")
    private boolean selfAssignable;

    /**
     * If the assignable role should be automatically
     * applied when attains any skill milestone requirements.
     */
    @Column(name = "auto_assign")
    private boolean autoAssign;

    /** Should this role be applied to users automatically on join. */
    @Column(name = "on_user_join")
    private boolean onUserJoin;

    /** Should this role be applied to bots automatically on join. */
    @Column(name = "on_bot_join")
    private boolean onBotJoin;

    @OneToMany(targetEntity = SkillMilestone.class, mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkillMilestone> skillMilestones;

    public RoleData() {
        skillMilestones = new ArrayList<>();
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

    public boolean isAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(boolean autoAssign) {
        this.autoAssign = autoAssign;
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

    public List<SkillMilestone> getSkillMilestones() {
        return skillMilestones;
    }

    public void setSkillMilestones(List<SkillMilestone> skillMilestones) {
        this.skillMilestones = skillMilestones;
    }
}
