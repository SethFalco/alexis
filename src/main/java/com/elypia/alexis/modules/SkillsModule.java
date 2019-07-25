/*
 * Copyright (C) 2019  Elypia
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

package com.elypia.alexis.modules;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.SkillEntry;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Channels;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.*;

@Module(id = "Skills", group = "Settings", aliases = {"skill", "skills"}, help = "skills.h")
public class SkillsModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public SkillsModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "skills.list", aliases = {"list", "show"}, help = "skills.list.h")
    public Object list(@Channels(ChannelType.TEXT) JDACEvent event) {
        GuildData data = GuildData.query(event.asMessageRecieved().getGuild().getIdLong());
        List<SkillEntry> skills = data.getSettings().getSkills();

        if (skills.isEmpty())
            return scripts.get("skills.list.empty");

        StringJoiner joiner = new StringJoiner("\n");

        skills.forEach((skill) -> {
            joiner.add(skill.getName());
        });

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(scripts.get("skills"));
        builder.setDescription(joiner.toString());

        return builder;
    }

    @Command(id = "skills.info", aliases = "info", help = "skills.info.h")
    @Param(id = "common.name", help = "skills.info.p.name.h")
    public Object info(@Channels(ChannelType.TEXT) JDACEvent event, String name) {
        GuildData data = GuildData.query(event.asMessageRecieved().getGuild().getIdLong());
        List<SkillEntry> skills = data.getSettings().getSkills();
        Optional<SkillEntry> optSkill = SkillEntry.getSkill(skills, name);

        if (optSkill.isEmpty())
            return scripts.get("skills.info.not_exist");

        SkillEntry skill = optSkill.get();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(skill.getName());
        builder.setDescription(skill.getChannels().toString());
        builder.setFooter(String.valueOf(skill.isEnabled()), null);
        return builder;
    }

    @Command(id = "skills.create", aliases = {"create", "new"}, help = "skills.create.h")
    @Param(id = "common.name", help = "skills.create.p.name.h")
    public String create(@Channels(ChannelType.TEXT) JDACEvent event, String name) {
        GuildData data = GuildData.query(event.asMessageRecieved().getGuild().getIdLong());
        List<SkillEntry> skills = data.getSettings().getSkills();

        if (skills.stream().anyMatch((skill) -> skill.getName().equalsIgnoreCase(name)))
            return scripts.get("skills.create.skill_exists");

        skills.add(new SkillEntry(name));
        data.commit();

        return scripts.get("skills.create.success");
    }

    @Command(id = "skills.delete", aliases = {"delete", "del", "remove"}, help = "skills.delete.h")
    @Param(id = "common.name", help = "skills.delete.p.name.h")
    public String delete(@Channels(ChannelType.TEXT) JDACEvent event, String name) {
        GuildData data = GuildData.query(event.asMessageRecieved().getGuild().getIdLong());
        List<SkillEntry> skills = data.getSettings().getSkills();
        Optional<SkillEntry> optSkill = SkillEntry.getSkill(skills, name);

        if (optSkill.isEmpty())
            return scripts.get("skills.info.not_exist");

        skills.remove(optSkill.get());
        data.commit();
        return scripts.get("skills.delete.success");
    }

    @Command(id = "skills.prune", aliases = "prune", help = "skills.prune")
    public String prune(@Channels(ChannelType.TEXT) JDACEvent event) {
        GuildData data = GuildData.query(event.asMessageRecieved().getGuild().getIdLong());
        List<SkillEntry> skills = data.getSettings().getSkills();

        if (skills.isEmpty())
            return scripts.get("skills.list.empty");

        skills.clear();
        data.commit();

        return scripts.get("skills.prune.success");
    }

    @Command(id = "skills.assign", aliases = "assign", help = "skills.assign.h")
    @Param(id = "common.name", help = "skills.assign.p.name.h")
    @Param(id = "common.channel", help = "skills.assign.p.channel.h")
    public String assign(@Channels(ChannelType.TEXT) JDACEvent event, String name, @Search(Scope.LOCAL) TextChannel channel) {
        GuildData data = GuildData.query(event.asMessageRecieved().getGuild().getIdLong());
        List<SkillEntry> skills = data.getSettings().getSkills();
        Optional<SkillEntry> optSkill = SkillEntry.getSkill(skills, name);

        if (optSkill.isEmpty())
            return scripts.get("skills.info.not_exist");

        SkillEntry entry = optSkill.get();
        entry.getChannels().add(channel.getIdLong());
        data.commit();

        return scripts.get("skills.assign.success");
    }

    @Command(id = "skills.notify", aliases = "notify", help = "skills.notify.h")
    @Param(id = "common.name", help = "skills.notify.p.name.h")
    @Param(id = "common.toggle", help = "skills.notify.p.toggle.h")
    public String notify(@Channels(ChannelType.TEXT) JDACEvent event, String name, boolean enable) {
        GuildData data = getGuildData(event);
        List<SkillEntry> skills = data.getSettings().getSkills();
        Optional<SkillEntry> optSkill = SkillEntry.getSkill(skills, name);

        if (optSkill.isEmpty())
            return scripts.get("skills.info.not_exist");

        SkillEntry entry = optSkill.get();
        var params = Map.of("enabled", enable);

        if (entry.isNotify() == enable)
            return scripts.get("skills.notify.same", params);

        entry.setNotify(enable);
        data.commit();

        return scripts.get("skills.notify.success", params);
    }

    private GuildData getGuildData(JDACEvent event) {
        return GuildData.query(event.asMessageRecieved().getGuild().getIdLong());
    }
}
