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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.persistence.enums.Feature;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.repositories.*;
import org.elypia.alexis.discord.models.*;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@CommandController
@StandardCommand
public class EmoteController implements Controller {

    private final GuildRepository guildRepo;
    private final EmoteRepository emoteRepo;
    private final AlexisMessages messages;

    @Inject
    public EmoteController(GuildRepository guildRepo, AlexisMessages messages, EmoteRepository emoteRepo) {
        this.guildRepo = guildRepo;
        this.emoteRepo = emoteRepo;
        this.messages = messages;
    }

    /**
     * List all emotes in the guild.
     *
     * @param guild
     * @return
     */
    @StandardCommand
    public String list(@Param (value = "${source.guild}", displayAs = "current guild") Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        if (count == 0)
            return messages.emoteGuildNoEmotes(guild.getName());

        StringBuilder builder = new StringBuilder();

        for (Emote emote : emotes)
            builder.append(emote.getAsMention());

        return builder.toString();
    }

    /**
     * Post an emote we can see, even if the user is unable to perform it.
     * TODO: send back emote directly, not the embed
     * @param message The message that triggered this event.
     * @param emote The emote the user would like to post.
     * @return
     */
    @StandardCommand
    public MessageEmbed post(Message message, @Param Emote emote) {
        EmbedBuilder builder = DiscordUtils.newEmbed(message);
        builder.setImage(emote.getImageUrl());
        return builder.build();
    }

    @StandardCommand
    public String setEmoteTracking(@Channels(ChannelType.TEXT) @Elevated Message message, @Param("true") boolean isEnabled) {
        GuildData data = guildRepo.findBy(message.getGuild().getIdLong());
        Map<Feature, GuildFeature> features = data.getFeatures();
        GuildFeature feature = features.get(Feature.COUNT_GUILD_EMOTE_USAGE);
        long userId = message.getAuthor().getIdLong();

        if (feature == null) {
            data.addFeature(new GuildFeature(data, Feature.COUNT_GUILD_EMOTE_USAGE, isEnabled, userId));
            guildRepo.save(data);
            return "I've added the setting now.";
        } else {
            if (feature.isEnabled() == isEnabled)
                return "That's what it's already set too. ^-^'";

            feature.setEnabled(isEnabled);
            feature.setModifiedBy(userId);
        }

        guildRepo.save(data);
        return "I've changed the setting ";
    }

    /**
     * TODO: This is broken to shit
     * Display the emotes with how frequently they get used.
     *
     * @param entries How many entries to display of the leaderboard.
     * @param guild The guild to display a leaderboard for.
     * @return The failure message, or a model representing a leaderboard.
     */
    @StandardCommand
    public Object getEmoteLeaderboard(@Param("10") int entries, @Param(value = "${source.guild}", displayAs = "current guild") Guild guild) {
        if (guild.getEmotes().isEmpty())
            return messages.emoteLeaderboardNoEmotes();

        long guildId = guild.getIdLong();
        GuildData guildData = guildRepo.findBy(guildId);
        List<EmoteUsage> allEmoteUsages = guildData.getEmoteUsages();

        if (allEmoteUsages.isEmpty())
            return messages.emoteLeaderboardNeverUsed();

        Map<Long, List<EmoteUsage>> grouped = allEmoteUsages.stream()
            .collect(Collectors.groupingBy((emoteUsage) -> emoteUsage.getEmoteData().getId()));

        List<EmoteLeaderboardEntryModel> models = new ArrayList<>();

        grouped.forEach((emoteId, usages) -> {
            if (usages.isEmpty())
                return;

            Emote emote = guild.getEmoteById(emoteId);

            if (emote == null)
                return;

            int local = usages.stream()
                .filter((usage) -> guild.getIdLong() == usage.getGuildData().getId())
                .mapToInt(EmoteUsage::getOccurences)
                .sum();

            int global = usages.stream()
                .mapToInt(EmoteUsage::getOccurences)
                .sum();

            models.add(new EmoteLeaderboardEntryModel(emote, local, global));
        });

        Collections.sort(models);
        List<EmoteLeaderboardEntryModel> limitedModels = models.subList(0, Math.min(entries, models.size()));
        return new EmoteLeaderboardModel(limitedModels);
    }
}
