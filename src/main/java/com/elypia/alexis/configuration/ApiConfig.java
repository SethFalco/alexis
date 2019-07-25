/*
 * Copyright (C) 2019  Elypia
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

package com.elypia.alexis.configuration;

import com.electronwill.nightconfig.core.conversion.Path;

import javax.inject.Singleton;

@Singleton
public class ApiConfig {

    /** The path to the Service Account Key JSON file. */
    @Path("google")
    private String google;

    /** The osu! API key. */
    @Path("osu")
    private String osu;

    /** Steam API key. */
    @Path("steam")
    private String steam;

    /** Cleverbot API key. */
    @Path("cleverbot")
    private String cleverbot;

    /** Twitch API credentials. */
    @Path("twitch")
    private TwitchConfig twitchConfig;

    public String getGoogle() {
        return google;
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

    public TwitchConfig getTwitch() {
        return twitchConfig;
    }
}
