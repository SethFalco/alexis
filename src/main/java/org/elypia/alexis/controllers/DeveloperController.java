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

package org.elypia.alexis.controllers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.comcord.Scope;
import org.elypia.comcord.annotations.Scoped;
import org.elypia.comcord.constraints.BotOwner;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.inject.Singleton;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class DeveloperController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(DeveloperController.class);

    /** When listing guilds, display each guild in this format. */
    private static final String format = "`%s | %s (%,d Members)`";

    public String guilds(@BotOwner ActionEvent<Event, Message> event) {
        StringJoiner joiner = new StringJoiner("\n");
        Collection<Guild> guilds = event.getSource().getJDA().getGuilds();

        for (Guild guild : guilds)
           joiner.add(String.format(format, guild.getId(), guild.getName(), guild.getMembers().size()));

        return joiner.toString();
    }

    public String leave(@BotOwner ActionEvent<Event, Message> event, @Scoped(inPrivate = Scope.GLOBAL, inGuild = Scope.LOCAL) Guild guild) {
        guild.leave().queue();
        return "Succesfully left the guild.";
    }

    public String rename(@BotOwner ActionEvent<Event, Message> event, String input) {
        event.getSource().getJDA().getSelfUser().getManager().setName(input).complete();
        return "Name was succesfully changed to " + input + ".";
    }

    public String avatar(@BotOwner ActionEvent<Event, Message> event, URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);
            event.getSource().getJDA().getSelfUser().getManager().setAvatar(icon).complete();
            return "Avatar was succefuly changed!";
        }
    }

    public void shutdown(@BotOwner ActionEvent<Event, Message> event) {
        JDA jda = event.getSource().getJDA();
        jda.shutdown();
        logger.info("Logging out of Discord.");
        System.exit(0);
    }

    /**
     * @param event
     * @return
     */
    public String clean(@BotOwner ActionEvent<Event, Message> event) {
        event.getSource().getJDA().getGuilds().forEach((guild) -> {
            //
        });

        return "Done!";
    }
}
