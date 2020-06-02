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
import org.elypia.alexis.persistence.entities.GuildData;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.comcord.EventUtils;
import org.elypia.commandler.annotation.stereotypes.Binder;
import org.elypia.commandler.api.HeaderBinder;
import org.elypia.commandler.event.Request;

import javax.inject.Inject;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Binder
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
        Guild guild = EventUtils.getGuild(source);

        if (guild == null)
            return null;

        GuildData data = guildRepo.findBy(guild.getIdLong());

        if (data == null)
            return null;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("GUILD_PREFIX", data.getPrefix());

        return headers;
    }
}
