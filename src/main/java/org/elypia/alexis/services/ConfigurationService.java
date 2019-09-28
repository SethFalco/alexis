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

package org.elypia.alexis.services;

import com.electronwill.nightconfig.core.conversion.*;
import org.elypia.alexis.configuration.*;

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
