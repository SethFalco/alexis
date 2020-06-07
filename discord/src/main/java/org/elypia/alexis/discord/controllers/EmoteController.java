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

import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.discord.models.*;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.enums.Feature;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.comcord.EventUtils;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;

import javax.inject.Inject;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController
public class EmoteController {

    private final GuildRepository guildRepo;
    private final AlexisMessages messages;

    @Inject
    public EmoteController(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.messages = messages;
    }

    /**
     * List all emotes in the guild.
     *
     * @param guild
     * @return
     */
    @StandardCommand
    public String listAllEmotes(@Param (value = "${source.guild}", displayAs = "current guild") Guild guild) {
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
     *
     * @param message The message that triggered this event.
     * @param emote The emote the user would like to post.
     * @return Either a {@link MessageEmbed} or {@link String} response to the command.
     */
    @StandardCommand
    public Object postEmote(Message message, @Param Emote emote) {
        String emoteUrl = emote.getImageUrl();

        if (EventUtils.canSendEmbed(message))
            return DiscordUtils.newEmbed(message).setImage(emoteUrl).build();

        return messages.emotePostLackPermissions(emoteUrl);
    }

    // TODO: Multiple aliases prefix/commands
    @StandardCommand
    public String setEmoteTracking(@Channels(ChannelType.TEXT) @Elevated Message message, @Param("true") boolean isEnabled) {
        long guildId = message.getGuild().getIdLong();
        Optional<GuildData> optData = guildRepo.findOptionalBy(guildId);
        GuildData data = optData.orElse(new GuildData(guildId));

        Map<Feature, GuildFeature> features = data.getFeatures();
        GuildFeature feature = features.get(Feature.COUNT_GUILD_EMOTE_USAGE);
        long userId = message.getAuthor().getIdLong();

        if (feature == null) {
            features.put(Feature.COUNT_GUILD_EMOTE_USAGE, new GuildFeature(data, Feature.COUNT_GUILD_EMOTE_USAGE, isEnabled, userId));
            guildRepo.save(data);
            return messages.emoteTrackingEnabled();
        } else {
            if (feature.isEnabled() == isEnabled)
                return messages.emoteTrackingSetToSame();

            feature.setEnabled(isEnabled);
            feature.setModifiedBy(userId);
        }

        guildRepo.save(data);

        String enabledDisplay = (isEnabled) ? messages.enabled() : messages.disabled();
        return messages.emoteTrackingSettingChanged(enabledDisplay);
    }

    /**
     * TODO: Address default values where object isn't in DMs
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
        List<EmoteData> allEmotes = guildData.getEmotes();

        if (allEmotes.isEmpty())
            return messages.emoteLeaderboardNeverUsed();

        List<EmoteLeaderboardEntryModel> models = new ArrayList<>();

        for (EmoteData emoteData : allEmotes) {
            List<EmoteUsage> usages = emoteData.getUsages();

            if (usages.isEmpty())
                continue;

            Emote emote = guild.getEmoteById(emoteData.getId());

            if (emote == null)
                continue;

            int local = usages.stream()
                .filter((usage) -> guildId == usage.getUsageGuildData().getId())
                .mapToInt(EmoteUsage::getOccurences)
                .sum();

            int global = usages.stream()
                .mapToInt(EmoteUsage::getOccurences)
                .sum();

            models.add(new EmoteLeaderboardEntryModel(emote, local, global));
        }

        Collections.sort(models);
        List<EmoteLeaderboardEntryModel> limitedModels = models.subList(0, Math.min(entries, models.size()));
        return new EmoteLeaderboardModel(limitedModels);
    }
}
