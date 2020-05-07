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

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.Bag;
import org.elypia.alexis.entities.*;
import org.elypia.alexis.repositories.*;
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

    private final EmoteRepository emoteRepo;
    private final EmoteUsageRepository emoteUsageRepo;

    @Inject
    public EmoteListener(EmoteRepository emoteRepo, EmoteUsageRepository emoteUsageRepo) {
        this.emoteRepo = Objects.requireNonNull(emoteRepo);
        this.emoteUsageRepo = Objects.requireNonNull(emoteUsageRepo);
    }

    /**
     * // TODO: Let users decide if this is enabled or not, and if it should track guild emotes only, or all mutual emotes.
     * @param event
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        List<Emote> emotes = message.getEmotes();
        Bag<Emote> emotesBag = message.getEmotesBag();
        long eventGuildId = event.getGuild().getIdLong();

        for (Emote emote : emotes) {
            Guild guild = emote.getGuild();

            if (guild == null)
                return;

            long emoteId = emote.getIdLong();
            int count = emotesBag.getCount(emote);

            logger.debug("Inserting emote and usage to database with ID: {} and Count: {}", emoteId, count);

            EmoteData emoteData = new EmoteData(emoteId, guild.getIdLong());
            emoteRepo.save(emoteData);

            EmoteUsage emoteUsage = new EmoteUsage(emoteId, eventGuildId, count);
            emoteUsageRepo.save(emoteUsage);
        }
    }
}
