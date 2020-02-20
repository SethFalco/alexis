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
        guildRepo.updatePrefix(prefix, guildId);
        return "The prefix has been changed to " + prefix + ".";
    }

    public String enableMentionOnly(@Channels(ChannelType.TEXT) @Elevated @Database ActionEvent<Event, Message> event) {
        long guildId = event.getRequest().getMessage().getGuild().getIdLong();
        guildRepo.updatePrefix(null, guildId);
        return "I'll now only respond to mentions.";
    }
}
