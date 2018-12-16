package com.elypia.alexis.entities.embedded;

import xyz.morphia.annotations.Embedded;

@Embedded
public class LevelReward {

    /**
     * The name of this reward as displayed when checking
     * unlocks.
     */
    private String name;

    /**
     * The level the member must be to unlock this reward.
     */
    private int level;

    /**
     * The role the user should recieve upon reaching this level.
     */
    private long roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
