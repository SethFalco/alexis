package com.elypia.alexis.entities.embedded;

import net.dv8tion.jda.core.entities.Guild;
import xyz.morphia.annotations.*;

import java.util.*;

@Embedded
public class GuildSettings {

    /**
     * The configurable prefix for this {@link Guild}.
     */
    @Property("prefix")
    private String prefix;

    /**
     * The configuration for the Guild specific logging
     * settings. This allows guild owners to manage what channel
     * and what type of events are logged from Alexis to help
     * them manage.
     */
    @Embedded("log_settings")
    private GuildLogSettings logSettings;

    /**
     * Should Alexis save all roles obtained from users
     * and try give them back if a user leaves and comes back
     * at a later point.
     */
    @Property("previous_roles")
    private boolean previousRoles;

    /**
     * Configure join and leave messages in a guild
     * where users/bots may join and leave.
     */
    @Embedded("greetings")
    private GreetingSettings greetingSettings;

    /**
     * Configure the role users and/or bots may get automatically
     * upon joining the guild.
     */
    @Embedded("join_role")
    private JoinRoleSettings joinRoleSettings;

    /**
     * Configure level settings and when notifications
     * may appear.
     */
    @Embedded("levels")
    private LevelSettings levelSettings;

    /**
     * Configure music player settings, permissions
     * and and messages and events.
     */
    @Embedded("music")
    private MusicSettings musicSettings;

    /**
     * Configure translation settings associated with
     * the translate on reacting with a flag.
     */
    @Embedded("translate")
    private TranslateSettings translateSettings;

    /**
     * Guild configured skills that managers can create
     * to make specific skills to gain XP in and rewards
     * tailored to those skills.
     */
    @Embedded("skills")
    private List<SkillEntry> skills;

    @Embedded("assignable_roles")
    private List<AssignableRole> assignableRoles;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public GuildLogSettings getLogSettings() {
        if (logSettings == null)
            logSettings = new GuildLogSettings();

        return logSettings;
    }

    public void setLogSettings(GuildLogSettings logSettings) {
        this.logSettings = logSettings;
    }

    public boolean isPreviousRoles() {
        return previousRoles;
    }

    public void setPreviousRoles(boolean previousRoles) {
        this.previousRoles = previousRoles;
    }

    public GreetingSettings getGreetingSettings() {
        if (greetingSettings == null)
            greetingSettings = new GreetingSettings();

        return greetingSettings;
    }

    public void setGreetingSettings(GreetingSettings greetingSettings) {
        this.greetingSettings = greetingSettings;
    }

    public JoinRoleSettings getJoinRoleSettings() {
        if (joinRoleSettings == null)
            joinRoleSettings = new JoinRoleSettings();

        return joinRoleSettings;
    }

    public void setJoinRoleSettings(JoinRoleSettings joinRoleSettings) {
        this.joinRoleSettings = joinRoleSettings;
    }

    public LevelSettings getLevelSettings() {
        if (levelSettings == null)
            levelSettings = new LevelSettings();

        return levelSettings;
    }

    public void setLevelSettings(LevelSettings levelSettings) {
        this.levelSettings = levelSettings;
    }

    public MusicSettings getMusicSettings() {
        if (musicSettings == null)
            musicSettings = new MusicSettings();

        return musicSettings;
    }

    public void setMusicSettings(MusicSettings musicSettings) {
        this.musicSettings = musicSettings;
    }

    public TranslateSettings getTranslateSettings() {
        if (translateSettings == null)
            translateSettings = new TranslateSettings();

        return translateSettings;
    }

    public void setTranslateSettings(TranslateSettings translateSettings) {
        this.translateSettings = translateSettings;
    }

    public List<SkillEntry> getSkills() {
        if (skills == null)
            skills = new ArrayList<>();

        return skills;
    }

    public void setSkills(List<SkillEntry> skills) {
        this.skills = skills;
    }

    public List<AssignableRole> getAssignableRoles() {
        if (assignableRoles == null)
            assignableRoles = new ArrayList<>();

        return assignableRoles;
    }

    public void setAssignableRoles(List<AssignableRole> assignableRoles) {
        this.assignableRoles = assignableRoles;
    }
}
