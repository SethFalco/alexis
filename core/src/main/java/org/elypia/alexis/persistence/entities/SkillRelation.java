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
@Table(name = "skill_relation")
public class SkillRelation implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue
    @Column(name = "relation_id")
    private int id;

    @Column(name = "skill_id")
    private int skillId;

    @Column(name = "text_channel_id")
    private long channelId;

    public SkillRelation() {
        // Do nothing
    }

    public SkillRelation(int skillId, long channelId) {
        this.skillId = skillId;
        this.channelId = channelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }
}
