package com.elypia.alexis.entities.embedded;

import com.elypia.alexis.entities.MemberData;
import xyz.morphia.annotations.Embedded;

import java.util.*;

/**
 * A skill entry is a skill created by a guild locally
 * with any associated data. Actual member XP is stored in
 * {@link MemberData}.
 */
@Embedded
public class SkillEntry {

    /**
     * The unique (to the guild) name of the skill.
     */
    private String name;

    /**
     * The text channels associated with this skill,
     * you will gain XP in this skill if you talk in any
     * of these channels.
     */
    private List<Long> channels;

    /**
     * If false (disabled), will ensure the skill is
     * <strong>NOT</strong> deleted, but disabled and hidden from users.
     */
    private boolean enabled;

    /**
     * Should the guild be notified in the channel
     * the user levels up in.
     */
    private boolean notify;

    private List<LevelReward> rewards;

    public static Optional<SkillEntry> getSkill(List<SkillEntry> skills, String name) {
        for (SkillEntry skill : skills) {
            if (skill.getName().equalsIgnoreCase(name))
                return Optional.of(skill);
        }

        return Optional.empty();
    }

    public SkillEntry() {

    }

    public SkillEntry(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getChannels() {
        if (channels == null)
            channels = new ArrayList<>();

        return channels;
    }

    public void setChannels(List<Long> channels) {
        this.channels = channels;
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

    public List<LevelReward> getRewards() {
        return rewards;
    }

    public void setRewards(List<LevelReward> rewards) {
        this.rewards = rewards;
    }
}
