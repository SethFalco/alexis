package com.elypia.alexis.config;

import com.electronwill.nightconfig.core.conversion.*;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.elypia.alexis.config.embedded.*;

public class ConfigurationService {

    @Path("name")
    private String applicationName;

    @Path("scripts")
    private ScriptsConfig scriptsConfig;

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

    @Path("debug")
    private DebugConfig debugConfig;

    public ConfigurationService(String path) {
        FileConfig fileConfig = FileConfig.of(path);
        fileConfig.load();

        var config = new ObjectConverter().toObject(fileConfig, ConfigurationService::new);
    }

    public ConfigurationService(ConfigurationService configuration) {
        this.applicationName = configuration.applicationName;
        this.scriptsConfig = configuration.scriptsConfig;
        this.databaseConfig = configuration.databaseConfig;
        this.discordConfig = configuration.discordConfig;
        this.apiCredentials = configuration.apiCredentials;
        this.debugConfig = configuration.debugConfig;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public ScriptsConfig getScriptsConfig() {
        return scriptsConfig;
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

    public DebugConfig getDebugConfig() {
        return debugConfig;
    }
}
