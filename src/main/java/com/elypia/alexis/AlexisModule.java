package com.elypia.alexis;

import com.elypia.alexis.config.*;
import com.google.inject.AbstractModule;
import org.apache.commons.cli.CommandLine;

public class AlexisModule extends AbstractModule {

    private final CommandLine commandLine;
    private final ConfigurationService configurationService;
    private final ApiCredentials apiCredentials;

    public AlexisModule(CommandLine commandLine, ConfigurationService configurationService) {
        this.commandLine = commandLine;
        this.configurationService = configurationService;
        this.apiCredentials = configurationService.getApiCredentials();
    }

    @Override
    protected void configure() {
        bind(CommandLine.class).toInstance(commandLine);
        bind(ConfigurationService.class).toInstance(configurationService);
        bind(ApiCredentials.class).toInstance(apiCredentials);
        bind(TwitchConfig.class).toInstance(apiCredentials.getTwitchConfig());
        bind(DatabaseConfig.class).toInstance(configurationService.getDatabaseConfig());
        bind(DiscordConfig.class).toInstance(configurationService.getDiscordConfig());
    }
}
