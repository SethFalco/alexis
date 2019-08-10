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

package com.elypia.alexis.modules;

import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.JDACHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

@Module(id = "Guild Logging", group = "Settings", aliases = {"log", "logging", "logger"}, help = "guild_logging.help")
public class GuildLogModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public GuildLogModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }
}
