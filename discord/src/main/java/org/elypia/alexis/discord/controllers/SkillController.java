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
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.repositories.*;
import org.elypia.comcord.constraints.Channels;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;

import javax.inject.Inject;
import java.util.*;

@StandardController
public class SkillController {

    private final AlexisMessages messages;
    private final GuildRepository guildRepo;
    private final MessageChannelRepository channelRepo;
    private final SkillRepository skillRepo;

    @Inject
    public SkillController(AlexisMessages messages, GuildRepository guildRepo, MessageChannelRepository channelRepo, SkillRepository skillRepo) {
        this.messages = messages;
        this.guildRepo = guildRepo;
        this.channelRepo = channelRepo;
        this.skillRepo = skillRepo;
    }

    @StandardCommand
    public Object listAllSkills(@Channels(ChannelType.TEXT) Message message) {
        long guildId = message.getGuild().getIdLong();
        List<Skill> skills = skillRepo.findByGuild(guildId);

        if (skills.isEmpty())
            return messages.skillGuildHasNoSkills();

        StringJoiner joiner = new StringJoiner("\n");
        skills.forEach((skill) -> joiner.add(skill.getName()));

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(messages.skillTitle());
        builder.setDescription(joiner.toString());

        return builder;
    }

    @StandardCommand
    public Object getSkillInfo(@Channels(ChannelType.TEXT) Message message, @Param String name) {
        long guildId = message.getGuild().getIdLong();
        Optional<Skill> optSkill = skillRepo.findByGuildAndNameEqualIgnoreCase(guildId, name);

        if (optSkill.isEmpty())
            return messages.skillNotFoundWithName(name);

        return optSkill.get();
    }

    @StandardCommand
    public String createSkill(@Channels(ChannelType.TEXT) Message message, @Param String name) {
        Guild guild = message.getGuild();
        long guildId = guild.getIdLong();
        int count = skillRepo.countByGuildIdAndNameEqualIgnoreCase(guildId, name);

        if (count > 0)
            return messages.skillWithNameAlreadyExists(name, guild.getName());

        GuildData guildData = guildRepo.findOptionalBy(guildId).orElse(new GuildData(guildId));
        guildData.getSkills().add(new Skill(guildData, name, true, true));
        guildRepo.save(guildData);

        return messages.skillAddedSuccesfully();
    }

    @StandardCommand
    public String deleteSkill(@Channels(ChannelType.TEXT) Message message, @Param String name) {
        long guildId = message.getGuild().getIdLong();
        GuildData guildData = guildRepo.findBy(guildId);
        List<Skill> skills = guildData.getSkills();

        Optional<Skill> optSkill = skills.stream()
            .filter((skill) -> skill.getName().equalsIgnoreCase(name))
            .findFirst();

        if (optSkill.isEmpty())
            return messages.skillDeletedNonExistingSkill();

        Skill skill = optSkill.get();
        skills.remove(skill);
        guildRepo.save(guildData);
        return messages.skillDeletedSuccesfully();
    }

    @StandardCommand
    public String pruneSkills(@Channels(ChannelType.TEXT) Message message) {
        long guildId = message.getGuild().getIdLong();
        GuildData guildData = guildRepo.findBy(guildId);
        List<Skill> skills = guildData.getSkills();

        if (skills.isEmpty())
            return messages.skillGuildHasNoSkills();

        int listSize = skills.size();
        skills.clear();
        guildRepo.save(guildData);

        return messages.skillDeleteAllSkills(listSize);
    }

    /**
     * Assigns a relationship between a {@link Skill} and {@link TextChannel}
     * so that users can gain XP from interactions in the channel.
     *
     * @param message The user interaction that triggered this command.
     * @param name The name of the skill, non-case-sensitive.
     * @param channel The channel the skill is to be assigned to.
     * @return The response for the command.
     */
    @StandardCommand
    public String assignSkillToChannel(@Channels(ChannelType.TEXT) Message message, @Param String name, @Param TextChannel channel) {
        long guildId = message.getGuild().getIdLong();
        Optional<Skill> optSkill = skillRepo.findByGuildAndNameEqualIgnoreCase(guildId, name);

        if (optSkill.isEmpty())
            return messages.skillNotFoundWithName(name);

        Skill skill = optSkill.get();
        GuildData guildData = skill.getGuild();

        long channelId = channel.getIdLong();

        MessageChannelData channelData = guildData.getMessageChannels().stream()
            .filter((cd) -> cd.getId() == channelId)
            .findAny()
            .orElse(new MessageChannelData(channelId, guildData));

        SkillRelation relation = new SkillRelation(skill, channelData);
        channelData.getSkillRelations().add(relation);
        channelRepo.save(channelData);

        return messages.skillAssignedToChannel(skill.getName(), channel.getAsMention());
    }

    @StandardCommand
    public String setNotify(@Channels(ChannelType.TEXT) Message message, @Param String name, @Param boolean isEnabled) {
        long guildId = message.getGuild().getIdLong();
        Optional<Skill> optSkill = skillRepo.findByGuildAndNameEqualIgnoreCase(guildId, name);

        if (optSkill.isEmpty())
            return messages.skillNotFoundWithName(name);

        Skill skill = optSkill.get();

        if (skill.isEnabled() == isEnabled)
            return messages.skillSettingNotChanged();

        skill.setEnabled(isEnabled);
        skillRepo.save(skill);

        return (isEnabled) ? messages.skillNotificationOn() : messages.skillNotificationOff();
    }
}
