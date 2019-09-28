/*
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis;

import com.google.inject.AbstractModule;
import org.apache.commons.cli.CommandLine;
import org.elypia.alexis.configuration.*;
import org.elypia.alexis.services.ConfigurationService;

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
