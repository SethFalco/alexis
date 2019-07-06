package com.elypia.alexis.database.entities;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "guild_feature")
@Table
public class DbGuildFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private int id;

    @Column(name = "guild_id")
    private int guildId;

    @Column(name = "feature_name")
    private String name;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "modified_by")
    private long modifiedBy;

    @Column(name = "modified_at")
    private Date modifiedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuildId() {
        return guildId;
    }

    public void setGuildId(int guildId) {
        this.guildId = guildId;
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

    public long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
