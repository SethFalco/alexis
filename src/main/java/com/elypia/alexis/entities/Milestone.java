package com.elypia.alexis.entities;

import javax.persistence.*;

@Entity(name = "skill_milestone")
@Table
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_ms_id")
    private int id;

    @Column(name = "skill_id")
    private int skillId;

    @Column(name = "skill_name")
    private int name;

    @Column(name = "skill_ms_xp")
    private int xp;

    public int getId() {
        return id;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
