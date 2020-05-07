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
@Table(name = "assignable_role")
public class AssignableRole implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "role_id")
    private long roleId;

    @Column(name = "skill_ms_id")
    private int skillMilestoneId;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getSkillMilestoneId() {
        return skillMilestoneId;
    }

    public void setSkillMilestoneId(int skillMilestoneId) {
        this.skillMilestoneId = skillMilestoneId;
    }
}
