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

package org.elypia.alexis.discord.listeners;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.elypia.alexis.entities.GuildData;
import org.elypia.alexis.repositories.*;
import org.slf4j.*;

import javax.annotation.Nonnull;
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

    private GuildRepository guildRepo;
    private EmoteRepository emoteRepo;

    @Inject
    public EntityListener(GuildRepository guildRepo, EmoteRepository emoteRepo) {
        this.guildRepo = guildRepo;
        this.emoteRepo = emoteRepo;
    }

    @Override
    public void onGenericGuild(@Nonnull GenericGuildEvent event) {
        long guildId = event.getGuild().getIdLong();
        GuildData guildData = new GuildData(guildId);
        guildRepo.save(guildData);
    }

    @Override
    public void onGenericEmote(GenericEmoteEvent event) {
        addEmotes(event, event.getEmote());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        addEmotes(event, event.getMessage().getEmotes());
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
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
