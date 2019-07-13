package com.elypia.alexis;

import com.elypia.alexis.configuration.*;
import com.google.inject.AbstractModule;
import org.apache.commons.cli.CommandLine;

public class AlexisModule extends AbstractModule {

    private final CommandLine commandLine;
    private final ConfigurationService configurationService;

    public AlexisModule(CommandLine commandLine, ConfigurationService configurationService) {
        this.commandLine = commandLine;
        this.configurationService = configurationService;
    }

    @Override
    protected void configure() {
        ApiCredentials apiCredentials = configurationService.getApiCredentials();

        bind(CommandLine.class).toInstance(commandLine);
        bind(ConfigurationService.class).toInstance(configurationService);
        bind(DatabaseConfig.class).toInstance(configurationService.getDatabaseConfig());
        bind(DiscordConfig.class).toInstance(configurationService.getDiscordConfig());
        bind(ApiCredentials.class).toInstance(apiCredentials);
        bind(TwitchConfig.class).toInstance(apiCredentials.getTwitchConfig());
    }
}
