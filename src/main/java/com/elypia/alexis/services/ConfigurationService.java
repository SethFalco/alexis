package com.elypia.alexis.services;

import com.electronwill.nightconfig.core.conversion.*;
import com.elypia.alexis.configuration.*;

import javax.inject.Singleton;

@Singleton
public class ConfigurationService {

    /**
     * The database configuration, this doesn't have to
     * be present if the database is disabled. This could be
     * because the database is down or disabled for debugging
     * purposes as ChatBot should be able to run with partial
     * functionality even without a database.
     */
    @Path("database")
    private DatabaseConfig database;

    /**
     * The database configuration, this must be present as it
     * has core settings that wouldn't allow the bot to run if
     * not known such as the {@link DiscordConfig#getToken()}  the bot token}.
     */
    @Path("discord")
    @SpecNotNull
    private DiscordConfig discord;

    @Path("api")
    private ApiConfig api;

    public ConfigurationService() {
        // Do nothing
    }

    public ConfigurationService(ConfigurationService configuration) {
        this.database = configuration.database;
        this.discord = configuration.discord;
        this.api = configuration.api;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }

    public DiscordConfig getDiscord() {
        return discord;
    }

    public ApiConfig getApi() {
        return api;
    }
}
