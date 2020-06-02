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
import org.elypia.alexis.persistence.enums.Feature;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.repositories.*;
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

    private final GuildRepository guildRepo;
    private final EmoteRepository emoteRepo;
    private final EmoteUsageRepository emoteUsageRepo;

    @Inject
    public EmoteListener(GuildRepository guildRepo, EmoteRepository emoteRepo, EmoteUsageRepository emoteUsageRepo) {
        this.guildRepo = Objects.requireNonNull(guildRepo);
        this.emoteRepo = Objects.requireNonNull(emoteRepo);
        this.emoteUsageRepo = Objects.requireNonNull(emoteUsageRepo);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        Message message = event.getMessage();
        List<Emote> emotes = message.getEmotes();

        if (emotes.isEmpty())
            return;

        Guild eventGuild = event.getGuild();
        long eventGuildId = eventGuild.getIdLong();
        GuildData guildData = guildRepo.findBy(eventGuildId);
        Map<Feature, GuildFeature> features = guildData.getFeatures();
        GuildFeature feature = features.get(Feature.COUNT_GUILD_EMOTE_USAGE);

        if (feature == null || !feature.isEnabled())
            return;

        Bag<Emote> emotesBag = message.getEmotesBag();

        for (Emote emote : emotes) {
            Guild guild = emote.getGuild();

            if (guild == null)
                return;

            long emoteId = emote.getIdLong();
            int count = emotesBag.getCount(emote);

            logger.debug("Inserting emote and usage to database with ID: {} and Count: {}", emoteId, count);

            EmoteData emoteData = new EmoteData(emoteId, guildData);
            emoteRepo.save(emoteData);

            EmoteUsage emoteUsage = new EmoteUsage(emoteData, guildData, count);
            emoteUsageRepo.save(emoteUsage);
        }
    }
}
