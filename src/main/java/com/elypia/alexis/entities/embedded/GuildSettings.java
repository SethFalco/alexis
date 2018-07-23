package com.elypia.alexis.entities.embedded;

import org.mongodb.morphia.annotations.*;

@Embedded
public class GuildSettings {

    @Property("prefix")
    private String prefix;

    @Embedded("log_settings")
    private GuildLogSettings logSettings;

    @Property("previous_roles")
    private boolean previousRoles;

    @Property("passive_emotes")
    private boolean passiveEmotes;

    @Embedded("greetings")
    private GreetingSettings greetingSettings;

    @Embedded("join_role")
    private JoinRoleSettings joinRoleSettings;

    @Embedded("levels")
    private LevelSettings levelSettings;

    @Embedded("music")
    private MusicSettings musicSettings;

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

    public boolean isPassiveEmotes() {
        return passiveEmotes;
    }

    public void setPassiveEmotes(boolean passiveEmotes) {
        this.passiveEmotes = passiveEmotes;
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
}
