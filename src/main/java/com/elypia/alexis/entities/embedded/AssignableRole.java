package com.elypia.alexis.entities.embedded;

import com.elypia.alexis.entities.data.Achievement;
import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class AssignableRole {

    /**
     * The id of the role that is self-assignable.
     */
    private long roleId;

    /**
     * The achievement required to self-assign this role.
     */
    private Achievement achievment;

    /**
     * The name of the skill that may be required in order
     * to self-assign the role. <br>
     * May return null if there are no requirements.
     */
    private String skillName;

    /**
     * The level required in the respective skill in order
     * to self-assign the role. <br>
     * Will return 0 if there is no level requirement.
     */
    private int level;

    public AssignableRole() {

    }

    public AssignableRole(long roleId) {
        this.roleId = roleId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public Achievement getAchievment() {
        return achievment;
    }

    public void setAchievment(Achievement achievment) {
        this.achievment = achievment;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
