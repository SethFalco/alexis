/*
 * Copyright (C) 2019-2019  Elypia
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

import com.electronwill.nightconfig.core.conversion.*;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class DiscordConfig {

    /** The bot token to authenticate to the Discord bot API. */
    @Path("token")
    @SpecNotNull
    private String token;

    /** The command prefix to execute commands. */
    @Path("prefix")
    @SpecNotNull
    private List<String> prefix;

    /** The ID to the bot developers/owners guild. */
    @Path("support_guild")
    private long supportGuild;

    public String getToken() {
        return token;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public long getSupportGuild() {
        return supportGuild;
    }
}
