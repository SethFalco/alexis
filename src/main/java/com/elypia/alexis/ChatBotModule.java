package com.elypia.alexis;

import com.elypia.alexis.configuration.*;
import com.elypia.alexis.services.ConfigurationService;
import com.google.inject.AbstractModule;
import org.apache.commons.cli.CommandLine;

public class ChatBotModule extends AbstractModule {

    private final CommandLine commandLine;
    private final ConfigurationService configurationService;

    public ChatBotModule(CommandLine commandLine, ConfigurationService configurationService) {
        this.commandLine = commandLine;
        this.configurationService = configurationService;
    }

    @Override
    protected void configure() {
        ApiConfig apiConfig = configurationService.getApi();

        bind(CommandLine.class).toInstance(commandLine);
        bind(ConfigurationService.class).toInstance(configurationService);
        bind(DatabaseConfig.class).toInstance(configurationService.getDatabase());
        bind(DiscordConfig.class).toInstance(configurationService.getDiscord());
        bind(ApiConfig.class).toInstance(apiConfig);
        bind(TwitchConfig.class).toInstance(apiConfig.getTwitch());
    }
}
