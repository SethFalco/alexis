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
        guildRepo.updateLocale(locale, guild.getIdLong());
        return "Updated the guild global locale to " + locale + ".";
    }

    public String setLocalLocale(ActionEvent<Event, Message> event, Locale locale) {
        Message message = event.getRequest().getMessage();
        MessageChannel channel = message.getChannel();

        MessageChannelData data = messageChannelRepo.findBy(channel.getIdLong());
        data.setLocale(locale);
        messageChannelRepo.save(data);

        return "This channel was overridden to use the locale " + locale + ".";
    }
}
