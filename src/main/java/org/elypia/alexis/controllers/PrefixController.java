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
import org.elypia.alexis.repositories.GuildRepository;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.*;
import javax.validation.constraints.Size;

@Singleton
public class PrefixController implements Controller {

    private final GuildRepository guildRepo;

    @Inject
    public PrefixController(final GuildRepository guildRepo) {
        this.guildRepo = guildRepo;
    }

    public String setPrefix(@Channels(ChannelType.TEXT) @Elevated @Database ActionEvent<Event, Message> event, @Size(min = 1, max = 32) String prefix) {
        long guildId = event.getRequest().getMessage().getGuild().getIdLong();
        guildRepo.updatePrefix(guildId, prefix);
        return "The prefix has been changed to " + prefix + ".";
    }

    public String enableMentionOnly(@Channels(ChannelType.TEXT) @Elevated @Database ActionEvent<Event, Message> event) {
        long guildId = event.getRequest().getMessage().getGuild().getIdLong();
        guildRepo.updatePrefix(guildId, null);
        return "I'll now only respond to mentions.";
    }
}
