/*
 * Alexis - A general purpose chatbot for Discord.
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

package org.elypia.alexis.config;

import org.elypia.commandler.config.ConfigService;

import javax.inject.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class ApiConfig {

    /** The osu! API key. */
    private final String osu;

    /** Steam API key. */
    private final String steam;

    /** Cleverbot API key. */
    private final String cleverbot;

    @Inject
    public ApiConfig(final ConfigService config) {
        osu = config.getString("alexis.api.osu");
        steam = config.getString("alexis.api.steam");
        cleverbot = config.getString("alexis.api.cleverbot");
    }

    public String getOsu() {
        return osu;
    }

    public String getSteam() {
        return steam;
    }

    public String getCleverbot() {
        return cleverbot;
    }
}
