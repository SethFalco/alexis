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
