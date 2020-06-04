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

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Entity
@Table(
    name = "skill",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"guild_id", "skill_name"})
    }
)
public class Skill implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private GuildData guild;

    @Column(name = "skill_name")
    private String name;

    @ColumnDefault("1")
    @Column(name = "skill_enabled")
    private boolean enabled;

    @ColumnDefault("1")
    @Column(name = "skill_notify")
    private boolean notify;

    @OneToMany(targetEntity = SkillRelation.class, mappedBy = "skill", cascade = CascadeType.ALL)
    private List<SkillRelation> relations;

    public Skill() {
        relations = new ArrayList<>();
    }

    public Skill(GuildData guild, String name) {
        this.guild = guild;
        this.name = name;
    }

    public Skill(GuildData guild, String name, boolean enabled) {
        this(guild, name);
        this.enabled = enabled;
    }

    public Skill(GuildData guild, String name, boolean enabled, boolean notify) {
        this(guild, name, enabled);
        this.notify = notify;
    }

    public int getId() {
        return id;
    }

    public GuildData getGuild() {
        return guild;
    }

    public void setGuild(GuildData guildData) {
        this.guild = guildData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public List<SkillRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<SkillRelation> relations) {
        this.relations = relations;
    }
}
