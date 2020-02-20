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

package org.elypia.alexis.listeners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.Bag;
import org.elypia.alexis.entities.EmoteUsage;
import org.elypia.alexis.services.DatabaseService;
import org.hibernate.Session;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * Count up all emotes used in guilds.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class EmoteListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EmoteListener.class);

    private final DatabaseService dbService;

    @Inject
    public EmoteListener(final DatabaseService dbService) {
        this.dbService = Objects.requireNonNull(dbService);

        if (dbService.isDisabled())
            logger.warn("DatabaseService is disabled, won't track emotes.");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (dbService.isDisabled())
            return;

        long guildId = event.getGuild().getIdLong();
        Message message = event.getMessage();
        List<Emote> emotes = message.getEmotes();
        Bag<Emote> emotesBag = message.getEmotesBag();

        try (Session session = dbService.open()) {
            session.beginTransaction();

            for (Emote emote : emotes) {
                long emoteId = emote.getIdLong();
                int count = emotesBag.getCount(emote);
                session.save(new EmoteUsage(emoteId, guildId, count));
            }

            session.getTransaction().commit();
        }
    }
}
