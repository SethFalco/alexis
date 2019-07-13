package com.elypia.alexis.entities;

import javax.persistence.*;

@Entity(name = "skill")
@Table
public class SkillData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private int id;

    @Column(name = "guild_id")
    private long guildId;

    @Column(name = "skill_name")
    private String name;

    @Column(name = "skill_enabled")
    private boolean enabled;

    @Column(name = "skill_notify")
    private boolean notify;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
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

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
