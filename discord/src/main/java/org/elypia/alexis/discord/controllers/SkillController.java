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
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.repositories.*;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.annotations.Scoped;
import org.elypia.comcord.constraints.Channels;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;

import javax.inject.Inject;
import java.util.*;

@CommandController
@StandardCommand
public class SkillController implements Controller {

    private final AlexisMessages messages;
    private final MessageChannelRepository messageChannelRepo;
    private final SkillRepository skillRepo;
    private final SkillRelationRepository skillRelationRepo;

    @Inject
    public SkillController(AlexisMessages messages, MessageChannelRepository messageChannelRepo, SkillRepository skillRepo, SkillRelationRepository skillRelationRepo) {
        this.messages = messages;
        this.messageChannelRepo = messageChannelRepo;
        this.skillRepo = skillRepo;
        this.skillRelationRepo = skillRelationRepo;
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
        Skill skill = skillRepo.findByGuildAndNameEqualIgnoreCase(guildId, name);

        if (skill == null)
            return messages.skillNotFoundWithName(name);

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(skill.getName());
//        builder.setDescription(skill.getChannels().toString());
        builder.setFooter(String.valueOf(skill.isEnabled()), null);
        return builder;
    }

    @StandardCommand
    public String createSkill(@Channels(ChannelType.TEXT) Message message, @Param String name) {
        Guild guild = message.getGuild();
        long guildId = guild.getIdLong();
        int count = skillRepo.countByGuildIdAndNameEqualIgnoreCase(guildId, name);

        if (count > 0)
            return messages.skillWithNameAlreadyExists(name, guild.getName());

        Skill skill = new Skill(new GuildData(guildId), name, true, true);
        skillRepo.save(skill);

        return messages.skillAddedSuccesfully();
    }

    // TODO: this is throwing an exception
    @StandardCommand
    public String deleteSkill(@Channels(ChannelType.TEXT) Message message, @Param String name) {
        long guildId = message.getGuild().getIdLong();
        int changes = skillRepo.deleteByGuildAndNameEqualIgnoreCase(guildId, name);

        if (changes == 0)
            return messages.skillDeletedNonExistingSkill();

        return messages.skillDeletedSuccesfully();
    }

    @StandardCommand
    public String pruneSkills(@Channels(ChannelType.TEXT) Message message) {
        long guildId = message.getGuild().getIdLong();
        int changes = skillRepo.deleteByGuild(guildId);

        if (changes == 0)
            return messages.skillGuildHasNoSkills();

        return messages.skillDeleteAllSkills(changes);
    }

    /**
     * TODO: Not checking if it already exists, throws exception if tried to add twice
     *
     * Assigns a relationship between a {@link Skill} and {@link TextChannel}
     * so that users can gain XP from interactions in the channel.
     *
     * @param message The user interaction that triggered this command.
     * @param name The name of the skill, non-case-sensitive.
     * @param channel The channel the skill is to be assigned to.
     * @return The response for the command.
     */
    @StandardCommand
    public String assignSkill(@Channels(ChannelType.TEXT) Message message, @Param String name, @Param @Scoped TextChannel channel) {
        Skill skill = skillRepo.findByGuildAndNameEqualIgnoreCase(message.getGuild().getIdLong(), name);

        if (skill == null)
            return messages.skillNotFoundWithName(name);

        long channelId = channel.getIdLong();
        messageChannelRepo.save(new MessageChannelData(channelId, new GuildData(channel.getGuild().getIdLong())));

        SkillRelation relation = new SkillRelation(skill.getId(), channelId);
        skillRelationRepo.save(relation);

        return messages.skillAssignedToChannel(skill.getName(), channel.getAsMention());
    }

    @StandardCommand
    public String setNotify(@Channels(ChannelType.TEXT) Message message, @Param String name, @Param boolean isEnabled) {
        long guildId = message.getGuild().getIdLong();
        Skill skill = skillRepo.findByGuildAndNameEqualIgnoreCase(guildId, name);

        if (skill == null)
            return messages.skillNotFoundWithName(name);

        if (skill.isEnabled() == isEnabled)
            return messages.skillSettingNotChanged();

        skill.setEnabled(isEnabled);
        skillRepo.save(skill);

        return (isEnabled) ? messages.skillNotificationOn() : messages.skillNotificationOff();
    }
}
