package com.elypia.alexis.entities;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class GuildSettings {

    @Property("prefix")
    private String prefix;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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
        return greetingSettings;
    }

    public void setGreetingSettings(GreetingSettings greetingSettings) {
        this.greetingSettings = greetingSettings;
    }

    public JoinRoleSettings getJoinRoleSettings() {
        return joinRoleSettings;
    }

    public void setJoinRoleSettings(JoinRoleSettings joinRoleSettings) {
        this.joinRoleSettings = joinRoleSettings;
    }

    public LevelSettings getLevelSettings() {
        return levelSettings;
    }

    public void setLevelSettings(LevelSettings levelSettings) {
        this.levelSettings = levelSettings;
    }
}
