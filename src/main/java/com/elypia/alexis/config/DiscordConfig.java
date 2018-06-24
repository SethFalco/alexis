package com.elypia.alexis.config;

import com.google.gson.annotations.SerializedName;
import net.dv8tion.jda.core.entities.Game;

import java.util.List;

public class DiscordConfig {

    @SerializedName("token")
    private String token;

    @SerializedName("prefix")
    private String prefix;

    @SerializedName("support_guild")
    private long supportGuild;

    @SerializedName("log_channel")
    private long logChannel;

    @SerializedName("enforce_prefix")
    private boolean enforcePrefix;

    @SerializedName("developers_only")
    private boolean developersOnly;

    @SerializedName("statuses")
    private List<Game> statuses;

    @SerializedName("authors")
    private List<Author> authors;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getSupportGuild() {
        return supportGuild;
    }

    public void setSupportGuild(long supportGuild) {
        this.supportGuild = supportGuild;
    }

    public long getLogChannel() {
        return logChannel;
    }

    public void setLogChannel(long logChannel) {
        this.logChannel = logChannel;
    }

    public boolean isEnforcePrefix() {
        return enforcePrefix;
    }

    public void setEnforcePrefix(boolean enforcePrefix) {
        this.enforcePrefix = enforcePrefix;
    }

    public boolean isDevelopersOnly() {
        return developersOnly;
    }

    public void setDevelopersOnly(boolean developersOnly) {
        this.developersOnly = developersOnly;
    }

    public List<Game> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Game> statuses) {
        this.statuses = statuses;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
