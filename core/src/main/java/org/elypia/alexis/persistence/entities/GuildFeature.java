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

import org.elypia.alexis.persistence.enums.Feature;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@DynamicInsert
@DynamicUpdate
@Entity
@Table(
    name = "guild_feature",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"guild_id", "feature"})
    }
)
public class GuildFeature implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private GuildData guildData;

    @Enumerated(EnumType.STRING)
    @Column(name = "feature", nullable = false)
    private Feature feature;

    @ColumnDefault("1")
    @Column(name = "enabled", nullable = false)
    private boolean isEnabled;

    /** The ID of the user this was last modified by. */
    @Column(name = "modified_by")
    private Long modifiedBy;

    /** The time this was last modified at. */
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false)
    private Date modifiedAt;

    public GuildFeature() {
        // Do nothing
    }

    public GuildFeature(GuildData guildData, Feature feature) {
        this.guildData = guildData;
        this.feature = feature;
    }

    public GuildFeature(GuildData guildData, Feature feature, boolean isEnabled) {
        this(guildData, feature);
        this.isEnabled = isEnabled;
    }

    public GuildFeature(GuildData guildData, Feature feature, boolean isEnabled, Long modifiedBy) {
        this(guildData, feature, isEnabled);
        this.modifiedBy = modifiedBy;
    }

    public GuildFeature(GuildData guildData, Feature feature, boolean isEnabled, Long modifiedBy, Date modifiedAt) {
        this(guildData, feature, isEnabled, modifiedBy);
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        final String format = "%s(ID: %d, Feature: %s, Enabled: %s)";
        return String.format(format, this.getClass(), id, feature, isEnabled);
    }

    public Integer getId() {
        return id;
    }

    public GuildData getGuildData() {
        return guildData;
    }

    public void setGuildData(GuildData guildData) {
        this.guildData = guildData;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
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
