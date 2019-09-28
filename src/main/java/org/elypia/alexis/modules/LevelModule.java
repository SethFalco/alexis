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

package org.elypia.alexis.modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.elypia.alexis.entities.GuildData;
import org.elypia.alexis.entities.embedded.MessageSettings;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.metadata.ModuleData;
import org.elypia.jdac.alias.*;
import org.elypia.jdac.validation.Channels;

@Module(id = "Levels", group = "Settings", aliases = {"level", "lvl"}, help = "level.help")
public class LevelModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public LevelModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "level.message.name", aliases = "message", help = "level.message.help")
    @Param(id = "level.param.message.help", help = "level.message.message.help")
    public String setMessage(@Channels(ChannelType.TEXT) JDACEvent event, String message) {
        long id = event.getSource().getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        settings.setEnabled(true);
        settings.setChannel(event.asMessageRecieved().getChannel().getIdLong());
        settings.setMessage(message);

        data.commit();

        return scripts.get(event.getSource(), "level.message.response");
    }

    @Command(id = "level.test.name", aliases = "test", help = "level.test.help")
    public String test(@Channels(ChannelType.TEXT) JDACEvent event) {
        GenericMessageEvent source = event.getSource();
        long id = source.getGuild().getIdLong();

        GuildData data = GuildData.query(id);
        MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
        String message = settings.getMessage();

        if (message == null || message.isEmpty())
            return scripts.get(source, "level.no_message");

        return scripts.get(source, message);
    }
}
