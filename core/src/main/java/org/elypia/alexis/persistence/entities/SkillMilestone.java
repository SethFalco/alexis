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
 * @author seth@elypia.org (Seth Falco)
 */
@Entity
@Table(name = "skill_milestone")
public class SkillMilestone implements Serializable {

    private static final long serialVersionUID = 1;

    /** The generated ID that represents this milestone. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_ms_id")
    private int id;

    /** The Skill this milestone belongs to. */
    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    /**
     * The role that this milestone may reward the user.
     * Can be null if this is just an achievement with no role as a reward.
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleData role;

    /** The name of this milestone, it should be an achievment link name. */
    @Column(name = "skill_ms_name")
    private String name;

    /**
     * The amount of XP required to attain this skill.
     * We're storing it as XP to ensure the scale is matches up
     * incase the level formula uses internally ever changes.
     */
    @Column(name = "skill_ms_xp")
    private int xpRequirement;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public RoleData getRole() {
        return role;
    }

    public void setRole(RoleData role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getXpRequirement() {
        return xpRequirement;
    }

    public void setXpRequirement(int xpRequirement) {
        this.xpRequirement = xpRequirement;
    }
}
