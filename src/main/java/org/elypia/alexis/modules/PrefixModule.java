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

package org.elypia.alexis.modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.elypia.alexis.constraints.Database;
import org.elypia.alexis.entities.GuildData;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.metadata.ModuleData;
import org.elypia.jdac.alias.*;
import org.elypia.jdac.validation.*;

import javax.validation.constraints.Size;

@Module(id = "Prefix", group = "Settings", aliases = "prefix", help = "prefix.h")
public class PrefixModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public PrefixModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "prefix.mention", aliases = {"mention", "mentiononly"}, help = "prefix.mention.h")
    public String mentionOnly(
        @Channels(ChannelType.TEXT) @Elevated @Database JDACEvent event
    ) {
        String mention = event.getSource().getGuild().getSelfMember().getAsMention();
        setPrefix(event, mention);

        return scripts.get("prefix.mention.response");
    }

    @Default
    @Command(id = "prefix.change", aliases = {"set", "prefix"}, help = "prefix.change.h")
    @Param(id = "prefix.change.p.prefix", help = "prefix.change.p.prefix.h")
    public String setPrefix(
        @Channels(ChannelType.TEXT) @Elevated @Database JDACEvent event,
        @Size(min = 1, max = 32) String prefix
    ) {
        long id = event.getSource().getGuild().getIdLong();
        GuildData data = GuildData.query(id);
        data.getSettings().setPrefix(prefix);
        data.commit();

        return scripts.get("prefix.change.response");
    }
}
