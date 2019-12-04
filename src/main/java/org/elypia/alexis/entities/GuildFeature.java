/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Entity(name = "guild_feature")
@Table
public class GuildFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private int id;

    @Column(name = "guild_id")
    private Long guildId;

    @Column(name = "feature_enum")
    private int featureEnum;

    @Column(name = "enabled")
    private boolean enabled;

    /** The ID of the user this was last modified by. */
    @Column(name = "modified_by")
    private Long modifiedBy;

    /** The time this was last modified at. */
    @Column(name = "modified_at")
    private Date modifiedAt;

    public int getId() {
        return id;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public int getFeature() {
        return featureEnum;
    }

    public void setFeature(int featureEnum) {
        this.featureEnum = featureEnum;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
