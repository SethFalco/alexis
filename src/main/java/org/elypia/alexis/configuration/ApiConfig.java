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

package org.elypia.alexis.configuration;

public class ApiConfig {

    /** The path to the Service Account Key JSON file. */
    private final String googleSaKey;

    /** The osu! API key. */
    private final String osu;

    /** Steam API key. */
    private final String steam;

    /** Cleverbot API key. */
    private final String cleverbot;

    /** Twitch API credentials. */
    private final TwitchConfig twitchConfig;

    public ApiConfig(final ConfigService config) {
        googleSaKey = config.getBoolean("api.google_enabled");
        osu = config.getString("osu_api_key");
        steam = config.getString("steam_api_key");
        cleverbot = config.getString("api.cleverbot_api_key");
        twitchConfig = new TwitchConfig(config.getString("api.twitch.api-key"), config.getString("api.twitch.api-secret"));
    }

    public String getGoogleSaKey() {
        return googleSaKey;
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
