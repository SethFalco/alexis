package com.elypia.alexis.config;

import com.electronwill.nightconfig.core.conversion.*;

import javax.inject.Singleton;

@Singleton
public class ConfigurationService {

    /**
     * The database configuration, this doesn't have to
     * be present if the database is disabled. This could be
     * because the database is down or disabled for debugging
     * purposes as Alexis should be able to run with partial
     * functionality even without a database.
     */
    @Path("database")
    private DatabaseConfig databaseConfig;

    /**
     * The database configuration, this must be present as it
     * has core settings that wouldn't allow the bot to run if
     * not known such as the {@link DiscordConfig#getToken()}  the bot token}.
     */
    @Path("discord")
    @SpecNotNull
    private DiscordConfig discordConfig;

    @Path("api")
    private ApiCredentials apiCredentials;

    public ConfigurationService() {
        // Do nothing
    }

    public ConfigurationService(ConfigurationService configuration) {
        this.databaseConfig = configuration.databaseConfig;
        this.discordConfig = configuration.discordConfig;
        this.apiCredentials = configuration.apiCredentials;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public DiscordConfig getDiscordConfig() {
        return discordConfig;
    }

    public ApiCredentials getApiCredentials() {
        return apiCredentials;
    }
}
