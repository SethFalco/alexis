/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.comcord.EventUtils;
import org.elypia.comcord.constraints.Channels;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class VoiceController implements Controller {

    public String mention(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, VoiceChannel[] channels) {
        Set<Member> members = new HashSet<>();
        Arrays.stream(channels).map(VoiceChannel::getMembers).forEach(members::addAll);
        Set<User> users = members.stream()
            .map(Member::getUser)
            .filter(Predicate.not(User::isBot))
            .collect(Collectors.toSet());

        Message message = EventUtils.getMessage(event.getRequest().getSource());

        if (users.remove(message.getAuthor()) && users.isEmpty())
            return "There's no one else in that channel with you though?";

        if (users.isEmpty())
            return "There's no one in that channel though?";

        return users.stream()
            .map(User::getAsMention)
            .collect(Collectors.joining());
    }
}
