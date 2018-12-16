package com.elypia.alexis.entities.impl;

import xyz.morphia.annotations.Property;

public abstract class Experienceable {

    @Property("xp")
    protected int xp;

    public int incrementXp(int rewarded) {
        xp += rewarded;
        return xp;
    }

    /**
     * Remove XP from an Experiencable entity.
     *
     * @param penalty The amount of XP to remove.
     * @return The new amount of XP.
     */
    public int decrementXp(int penalty) {
        xp -= penalty;
        return xp;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
