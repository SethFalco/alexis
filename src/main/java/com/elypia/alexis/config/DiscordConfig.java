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

    public String getPrefix() {
        return prefix;
    }

    public long getSupportGuild() {
        return supportGuild;
    }

    public long getLogChannel() {
        return logChannel;
    }

    public boolean isEnforcePrefix() {
        return enforcePrefix;
    }

    public boolean isDevelopersOnly() {
        return developersOnly;
    }

    public List<Game> getStatuses() {
        return statuses;
    }

    public List<Author> getAuthors() {
        return authors;
    }
}
