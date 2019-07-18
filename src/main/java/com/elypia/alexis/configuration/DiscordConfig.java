package com.elypia.alexis.configuration;

import com.electronwill.nightconfig.core.conversion.*;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class DiscordConfig {

    /** The bot token to authenticate to the Discord bot API. */
    @Path("token")
    @SpecNotNull
    private String token;

    /** The command prefix to execute commands. */
    @Path("prefix")
    @SpecNotNull
    private List<String> prefix;

    /** The ID to the bot developers/owners guild. */
    @Path("support_guild")
    private long supportGuild;

    public String getToken() {
        return token;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public long getSupportGuild() {
        return supportGuild;
    }
}
