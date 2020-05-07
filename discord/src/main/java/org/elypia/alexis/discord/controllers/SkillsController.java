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
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.entities.*;
import org.elypia.alexis.repositories.*;
import org.elypia.comcord.EventUtils;
import org.elypia.comcord.annotations.Scoped;
import org.elypia.comcord.constraints.Channels;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class SkillsController implements Controller {

    private final SkillRepository skillRepo;
    private final SkillRelationRepository skillRelationRepo;

    @Inject
    public SkillsController(SkillRepository skillRepo, SkillRelationRepository skillRelationRepo) {
        this.skillRepo = skillRepo;
        this.skillRelationRepo = skillRelationRepo;
    }

    public Object listAllSkills(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event) {
        Event source = event.getRequest().getSource();
        Guild guild = EventUtils.getGuild(source);
        List<Skill> skills = skillRepo.findByGuildId(guild.getIdLong());

        if (skills.isEmpty())
            return "This guild has no skills configured.";

        StringJoiner joiner = new StringJoiner("\n");
        skills.forEach((skill) -> joiner.add(skill.getName()));

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Skills");
        builder.setDescription(joiner.toString());

        return builder;
    }

    public Object getSkillInfo(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, String name) {
        Event source = event.getRequest().getSource();
        Guild guild = EventUtils.getGuild(source);
        Skill skill = skillRepo.findByGuildIdAndNameEqualIgnoreCase(guild.getIdLong(), name);

        if (skill == null)
            return "No skill with that name exists for this guild.";

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(skill.getName());
//        builder.setDescription(skill.getChannels().toString());
        builder.setFooter(String.valueOf(skill.isEnabled()), null);
        return builder;
    }

    public String createSkill(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, String name) {
        Event source = event.getRequest().getSource();
        Guild guild = EventUtils.getGuild(source);

        int count = skillRepo.countByGuildIdAndNameEqualIgnoreCase(guild.getIdLong(), name);

        if (count > 0)
            return "A skill with that name already exists.";

        Skill skill = new Skill(guild.getIdLong(), name);
        skillRepo.save(skill);

        return "I've added the new skill now!";
    }

    public String deleteSkill(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, String name) {
        Event source = event.getRequest().getSource();
        Guild guild = EventUtils.getGuild(source);

        int changes = skillRepo.deleteByGuildIdAndNameEqualIgnoreCase(guild.getIdLong(), name);

        if (changes == 0)
            return "I don't have a skill with that name anyways.";

        return "Done, I've removed and deleted any data related to this skill.";
    }

    public String pruneSkills(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event) {
        Event source = event.getRequest().getSource();
        Guild guild = EventUtils.getGuild(source);

        int changes = skillRepo.deleteByGuildId(guild.getIdLong());

        if (changes == 0)
            return "You had no skills anyways.";

        return "I've deleted all " + changes + " skills that you had configured in this guild.";
    }

    /**
     * Assigns a relationship between a {@link Skill} and {@link TextChannel}
     * so that users can gain XP from interactions in the channel.
     *
     * @param event The user interaction that triggered this command.
     * @param name The name of the skill, non-case-sensitive.
     * @param channel The channel the skill is to be assigned to.
     * @return The response for the command.
     */
    public String assignSkill(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, String name, @Scoped TextChannel channel) {
        Event source = event.getRequest().getSource();
        Guild guild = EventUtils.getGuild(source);

        Skill skill = skillRepo.findByGuildIdAndNameEqualIgnoreCase(guild.getIdLong(), name);

        if (skill == null)
            return "There is no skill with that name.";

        SkillRelation relation = new SkillRelation(skill.getId(), channel.getIdLong());
        skillRelationRepo.save(relation);

        return "I've assigned " + skill.getName() + " to the text channel " + channel.getName() + ".";
    }

    public String setNotify(@Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, String name, boolean enable) {
        Event source = event.getRequest().getSource();
        Guild guild = EventUtils.getGuild(source);

        Skill skill = skillRepo.findByGuildIdAndNameEqualIgnoreCase(guild.getIdLong(), name);

        if (skill == null)
            return "There is no skill with that name.";

        if (skill.isEnabled() == enable)
            return "The skill is already set to that setting.";

        skill.setEnabled(enable);
        skillRepo.save(skill);

        return "I've updated the skill.";
    }
}
