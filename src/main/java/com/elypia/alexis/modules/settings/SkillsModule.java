package com.elypia.alexis.modules.settings;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.SkillEntry;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.param.Search;
import com.elypia.jdac.alias.JDACHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.util.*;

@Channel(ChannelType.TEXT)
@Module(name = "skills", group = "Settings", aliases = {"skill", "skills"}, help = "skills.h")
public class SkillsModule extends JDACHandler {

    @Command(name = "skills.list", aliases = {"list", "show"}, help = "skills.list.h")
    public Object list(JDACommand command) {
        GuildData data = getGuildData(command);
        List<SkillEntry> skills = data.getSettings().getSkills();

        if (skills.isEmpty())
            return command.getScript("skills.list.empty");

        StringJoiner joiner = new StringJoiner("\n");

        skills.forEach((skill) -> {
            joiner.add(skill.getName());
        });

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(command.getScript("skills"));
        builder.setDescription(joiner.toString());

        return builder;
    }

    @Command(name = "skills.info", aliases = "info", help = "skills.info.h")
    @Param(name = "common.name", help = "skills.info.p.name.h")
    public Object info(JDACommand command, String name) {
        GuildData data = getGuildData(command);
        List<SkillEntry> skills = data.getSettings().getSkills();
        Optional<SkillEntry> optSkill = SkillEntry.getSkill(skills, name);

        if (!optSkill.isPresent())
            return command.getScript("skills.info.not_exist");

        SkillEntry skill = optSkill.get();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(skill.getName());
        builder.setDescription(skill.getChannels().toString());
        builder.setFooter(String.valueOf(skill.isEnabled()), null);
        return builder;
    }

    @Command(name = "skills.create", aliases = {"create", "new"}, help = "skills.create.h")
    @Param(name = "common.name", help = "skills.create.p.name.h")
    public String create(JDACommand command, String name) {
        GuildData data = getGuildData(command);
        List<SkillEntry> skills = data.getSettings().getSkills();

        if (skills.stream().anyMatch((skill) -> skill.getName().equalsIgnoreCase(name)))
            return command.getScript("skills.create.skill_exists");

        skills.add(new SkillEntry(name));
        data.commit();

        return command.getScript("skills.create.success");
    }

    @Command(name = "skills.delete", aliases = {"delete", "del", "remove"}, help = "skills.delete.h")
    @Param(name = "common.name", help = "skills.delete.p.name.h")
    public String delete(JDACommand command, String name) {
        GuildData data = getGuildData(command);
        List<SkillEntry> skills = data.getSettings().getSkills();
        Optional<SkillEntry> optSkill = SkillEntry.getSkill(skills, name);

        if (!optSkill.isPresent())
            return command.getScript("skills.info.not_exist");

        skills.remove(optSkill.get());
        data.commit();
        return command.getScript("skills.delete.success");
    }

    @Command(name = "skills.prune", aliases = "prune", help = "skills.prune")
    public String prune(JDACommand command) {
        GuildData data = getGuildData(command);
        List<SkillEntry> skills = data.getSettings().getSkills();

        if (skills.isEmpty())
            return command.getScript("skills.list.empty");

        skills.clear();
        data.commit();

        return command.getScript("skills.prune.success");
    }

    @Command(name = "skills.assign", aliases = "assign", help = "skills.assign.h")
    @Param(name = "common.name", help = "skills.assign.p.name.h")
    @Param(name = "common.channel", help = "skills.assign.p.channel.h")
    public String assign(JDACommand command, String name, @Search(Scope.LOCAL) TextChannel channel) {
        GuildData data = getGuildData(command);
        List<SkillEntry> skills = data.getSettings().getSkills();
        Optional<SkillEntry> optSkill = SkillEntry.getSkill(skills, name);

        if (!optSkill.isPresent())
            return command.getScript("skills.info.not_exist");

        SkillEntry entry = optSkill.get();
        entry.getChannels().add(channel.getIdLong());
        data.commit();

        return command.getScript("skills.assign.success");
    }

    @Command(name = "skills.notify", aliases = "notify", help = "skills.notify.h")
    @Param(name = "common.name", help = "skills.notify.p.name.h")
    @Param(name = "common.toggle", help = "skills.notify.p.toggle.h")
    public String notify(JDACommand command, String name, boolean enable) {
        GuildData data = getGuildData(command);
        List<SkillEntry> skills = data.getSettings().getSkills();
        Optional<SkillEntry> optSkill = SkillEntry.getSkill(skills, name);

        if (!optSkill.isPresent())
            return command.getScript("skills.info.not_exist");

        SkillEntry entry = optSkill.get();
        var params = Map.of("enabled", enable);

        if (entry.isNotify() == enable)
            return command.getScript("skills.notify.same", params);

        entry.setNotify(enable);
        data.commit();

        return command.getScript("skills.notify.success", params);
    }

    private GuildData getGuildData(JDACommand command) {
        return GuildData.query(command.getSource().getGuild().getIdLong());
    }
}
