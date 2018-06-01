package com.elypia.alexis.discord.entities;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

@Embedded
public class JoinRoleSettings {

    @Property("apply_bot_to_user")
    private boolean applyUserToBot;

    @Embedded("user")
    private RoleSettings userSettings;

    @Embedded("bot")
    private RoleSettings botSettings;

    public boolean applyUserToBot() {
        return applyUserToBot;
    }

    public void setApplyUserToBot(boolean applyUserToBot) {
        this.applyUserToBot = applyUserToBot;
    }

    public RoleSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(RoleSettings userSettings) {
        this.userSettings = userSettings;
    }

    public RoleSettings getBotSettings() {
        return botSettings;
    }

    public void setBotSettings(RoleSettings botSettings) {
        this.botSettings = botSettings;
    }
}
