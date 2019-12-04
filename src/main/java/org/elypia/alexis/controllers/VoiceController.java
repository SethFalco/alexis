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
import org.elypia.comcord.EventUtils;
import org.elypia.comcord.constraints.Channels;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Singleton;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class VoiceController implements Controller {

    public String mention(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, VoiceChannel[] channels) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream()
            .map(Member::getUser)
            .filter(Predicate.not(User::isBot))
            .collect(Collectors.toSet());

        Message message = EventUtils.getMessage(event.getRequest().getSource());

        if (users.remove(message.getAuthor()) && users.size() == 0)
            return "There's no one else in that channel with you though?";

        if (users.size() == 0)
            return "There's no one in that channel though?";

        return users.stream()
            .map(User::getAsMention)
            .collect(Collectors.joining());
    }
}
