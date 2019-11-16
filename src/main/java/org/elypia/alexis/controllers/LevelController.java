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

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.constraints.Database;
import org.elypia.alexis.services.DatabaseService;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class LevelController implements Controller {

    private final DatabaseService dbService;

    @Inject
    public LevelController(final DatabaseService dbService) {
        this.dbService = dbService;
    }

    public String message(@Database @Elevated @Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, String message) {
//        Guild guild = EventUtils.getGuild(event.getSource());
//        long id = guild.getIdLong();
//
//        try (Session session = dbService.open()) {
//            GuildData data = session.get(GuildData.class, id);
//
//            if (data == null)
//                data = new GuildData(id);
//
//
//            MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
//            settings.setEnabled(true);
//            settings.setChannel(event.asMessageRecieved().getChannel().getIdLong());
//            settings.setMessage(message);
//
//            data.commit();
//        }
//
//
//        return scripts.get(event.getSource(), "level.message.response");
        return null;
    }

    public String test(@Database @Elevated @Channels(ChannelType.TEXT) ActionEvent<Event, Message> event) {
//        Guild guild = EventUtils.getGuild(event.getSource());
//        long id = guild.getIdLong();
//
//        try (Session session = dbService.open()) {
//            GuildData data = session.get(GuildData.class, id);
//
//            if (data == null)
//                return "You haven't configured any settings yet, maybe check them out first?";
//
//            MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
//            String message = settings.getMessage();
//
//            if (message == null || message.isEmpty())
//                return scripts.get(source, "level.no_message");
//
//            return scripts.get(source, message);
//        }
        return null;
    }
}
