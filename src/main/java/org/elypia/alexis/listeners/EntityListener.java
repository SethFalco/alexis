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

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.elypia.alexis.services.DatabaseService;
import org.slf4j.*;

import javax.inject.*;
import java.util.*;

/**
 * Find new entities that are using this application
 * for the first time and add them to the bot.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class EntityListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(EntityListener.class);

    private final DatabaseService dbService;

    @Inject
    public EntityListener(final DatabaseService dbService) {
        this.dbService = Objects.requireNonNull(dbService);

        if (dbService.isDisabled())
            logger.warn("DatabaseService is disabled, won't add entities.");
    }

    @Override
    public void onGenericEmote(GenericEmoteEvent event) {
        if (dbService.isDisabled())
            return;

        addEmotes(event, event.getEmote());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (dbService.isDisabled())
            return;

        addEmotes(event, event.getMessage().getEmotes());
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (dbService.isDisabled())
            return;

        addEmotes(event, event.getMessage().getEmotes());
    }

    private void addEmotes(Event event, Emote... emotes) {
        addEmotes(event, List.of(emotes));
    }

    private void addEmotes(Event event, Collection<Emote> emotes) {
//        try (Session session = dbService.open()) {
//            for (Emote emote : emotes) {
//                if (emote.isFake())
//                    emote = event.getJDA().getEmoteById(emote.getIdLong());
//
//                if (emote == null)
//                    continue;
//
//                session.saveOrUpdate(new EmoteData(emote.getIdLong(), emote.getGuild().getIdLong()));
//            }
//        }
    }
}
