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

package org.elypia.alexis.discord.binders;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import org.elypia.alexis.entities.GuildData;
import org.elypia.alexis.repositories.GuildRepository;
import org.elypia.commandler.Request;
import org.elypia.commandler.api.HeaderBinder;

import javax.inject.*;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class GuildBinder implements HeaderBinder {

    /** To select data from the database. */
    private final GuildRepository guildRepo;

    @Inject
    public GuildBinder(final GuildRepository guildRepo) {
        this.guildRepo = guildRepo;
    }

    @Override
    public <S, M> Map<String, String> bind(Request<S, M> request) {
        Event source = (Event)request.getSource();

        if (!(source instanceof GenericGuildEvent))
            return null;

        Guild guild = ((GenericGuildEvent)source).getGuild();
        GuildData data = guildRepo.findBy(guild.getIdLong());

        HashMap<String, String> headers = new HashMap<>();
        headers.put("GUILD_PREFIX", data.getPrefix());

        return headers;
    }
}
