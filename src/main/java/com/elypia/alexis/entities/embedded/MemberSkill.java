package com.elypia.alexis.entities.embedded;

import com.elypia.alexis.entities.impl.Experienceable;
import xyz.morphia.annotations.Embedded;

@Embedded
public class MemberSkill extends Experienceable {

    private String name;

    public MemberSkill() {

    }

    public MemberSkill(String name) {
        this(name, 0);
    }

    public MemberSkill(String name, int xp) {
        this.name = name;
        this.xp = xp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
