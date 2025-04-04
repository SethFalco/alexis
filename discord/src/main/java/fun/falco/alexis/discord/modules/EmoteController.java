/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.discord.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.elypia.comcord.EventUtils;
import org.elypia.comcord.constraints.Channels;
import org.elypia.comcord.constraints.Elevated;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.core.persistence.entities.EmoteData;
import fun.falco.alexis.core.persistence.entities.EmoteUsage;
import fun.falco.alexis.core.persistence.entities.FeatureSettings;
import fun.falco.alexis.core.persistence.entities.GuildData;
import fun.falco.alexis.core.persistence.enums.Feature;
import fun.falco.alexis.core.persistence.repositories.GuildRepository;
import fun.falco.alexis.discord.models.EmoteLeaderboardEntryModel;
import fun.falco.alexis.discord.models.EmoteLeaderboardModel;
import fun.falco.alexis.discord.utils.DiscordUtils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author seth@falco.fun (Seth Falco)
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
     * @param guild Guild to list all emotes form.
     * @return String where every emote has been mentioned from the guild.
     */
    @StandardCommand
    public String listAllEmotes(@Param (value = "${source.guild}", displayAs = "current guild") Guild guild) {
        List<Emote> emotes = guild.getEmotes();
        int count = emotes.size();

        if (count == 0) {
            return messages.emoteGuildNoEmotes(guild.getName());
        }

        StringBuilder builder = new StringBuilder();

        for (Emote emote : emotes) {
            builder.append(emote.getAsMention());
        }

        return builder.toString();
    }

    /**
     * Post an emote we can see, even if the user is unable to perform it.
     *
     * @param message Message that triggered this event.
     * @param emote Emote the user would like to post.
     * @return Either a {@link MessageEmbed} or {@link String} response to the command.
     */
    @StandardCommand
    public Object postEmote(Message message, @Param Emote emote) {
        String emoteUrl = emote.getImageUrl();

        if (EventUtils.canSendEmbed(message)) {
            return DiscordUtils.newEmbed(message).setImage(emoteUrl).build();
        }

        return messages.emotePostLackPermissions(emoteUrl);
    }

    @StandardCommand
    public String setEmoteTracking(@Channels(ChannelType.TEXT) @Elevated Message message, @Param("true") boolean isEnabled) {
        long guildId = message.getGuild().getIdLong();
        Optional<GuildData> optData = guildRepo.findOptionalBy(guildId);
        GuildData data = optData.orElse(new GuildData(guildId));

        Map<Feature, FeatureSettings> features = data.getFeatures();
        FeatureSettings feature = features.get(Feature.COUNT_GUILD_EMOTE_USAGE);
        long userId = message.getAuthor().getIdLong();

        if (feature == null) {
            features.put(Feature.COUNT_GUILD_EMOTE_USAGE, new FeatureSettings(data, Feature.COUNT_GUILD_EMOTE_USAGE, isEnabled, userId));
            guildRepo.save(data);
            return messages.emoteTrackingEnabled();
        } else {
            if (feature.isEnabled() == isEnabled) {
                return messages.emoteTrackingSetToSame();
            }

            feature.setEnabled(isEnabled);
            feature.setModifiedBy(userId);
        }

        guildRepo.save(data);

        String enabledDisplay = (isEnabled) ? messages.enabled() : messages.disabled();
        return messages.emoteTrackingSettingChanged(enabledDisplay);
    }

    /**
     * Display the emotes with how frequently they get used.
     *
     * @param entries How many entries to display of the leaderboard.
     * @param guild Guild to display a leaderboard for.
     * @return Failure message, or a model representing a leaderboard.
     */
    @StandardCommand
    public Object getEmoteLeaderboard(@Param("10") int entries, @Param(value = "${source.guild}", displayAs = "current guild") Guild guild) {
        if (guild.getEmotes().isEmpty()) {
            return messages.emoteLeaderboardNoEmotes();
        }

        long guildId = guild.getIdLong();
        GuildData guildData = guildRepo.findBy(guildId);
        List<EmoteData> allEmotes = guildData.getEmotes();

        if (allEmotes.isEmpty()) {
            return messages.emoteLeaderboardNeverUsed();
        }

        List<EmoteLeaderboardEntryModel> models = new ArrayList<>();

        for (EmoteData emoteData : allEmotes) {
            List<EmoteUsage> usages = emoteData.getUsages();

            if (usages.isEmpty()) {
                continue;
            }

            Emote emote = guild.getEmoteById(emoteData.getId());

            if (emote == null) {
                continue;
            }

            int local = usages.stream()
                .filter((usage) -> guildId == usage.getUsageGuildData().getId())
                .mapToInt(EmoteUsage::getOccurrences)
                .sum();

            int global = usages.stream()
                .mapToInt(EmoteUsage::getOccurrences)
                .sum();

            models.add(new EmoteLeaderboardEntryModel(emote, local, global));
        }

        Collections.sort(models);
        List<EmoteLeaderboardEntryModel> limitedModels = models.subList(0, Math.min(entries, models.size()));
        return new EmoteLeaderboardModel(limitedModels);
    }
}
