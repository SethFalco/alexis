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
import org.elypia.alexis.entities.MessageChannelData;
import org.elypia.alexis.repositories.*;
import org.elypia.comcord.constraints.Channels;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.*;
import java.util.Locale;

@Singleton
public class LocaleController implements Controller {

    private final GuildRepository guildRepo;
    private final MessageChannelRepository messageChannelRepo;

    @Inject
    public LocaleController(final GuildRepository guildRepo, final MessageChannelRepository messageChannelRepo) {
        this.guildRepo = guildRepo;
        this.messageChannelRepo = messageChannelRepo;
    }

    public String setGlobalLocale(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, Locale locale) {
        Guild guild = event.getRequest().getMessage().getGuild();
        guildRepo.updateLocale(guild.getIdLong(), locale);
        return "Updated the guild global locale to " + locale + ".";
    }

    public String setLocalLocale(ActionEvent<Event, Message> event, Locale locale) {
        Message message = event.getRequest().getMessage();
        MessageChannel channel = message.getChannel();

        MessageChannelData data = messageChannelRepo.findMessageChannelDataById(channel.getIdLong());
        data.setLocale(locale);
        messageChannelRepo.save(data);

        return "This channel was overridden to use the locale " + locale + ".";
    }
}
