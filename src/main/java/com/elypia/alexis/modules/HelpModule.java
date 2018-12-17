package com.elypia.alexis.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.ModulesContext;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.ICommandEvent;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;

@Module(id = "help.title", aliases = "help")
public class HelpModule extends JDACHandler {

    @Override
    @Default
    @Command(id = "help.modules.title", aliases = {"help", "halp", "helpme"})
    public Object help(ICommandEvent<GenericMessageEvent, Message> event) {
        return super.help(event);
    }

    @Overload("help.modules.title")
    @Param(id = "help.param.module.name", help = "help.param.module.help")
    public Object help(JDACEvent event, String module) {
        ModulesContext context = commandler.getContext();

        if (context.getAliases().contains(module)) {
            ModuleData moduleData = context.getModule(module);

            return event.trigger(moduleData.getAliases().get(0) + " help", false);
        }

        return scripts.get("help.module_not_found");
    }

    @Command(id = "Groups", aliases = {"group", "groups"}, help = "Display a list of all module groups.")
    public EmbedBuilder listGroups(JDACEvent event) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(scripts.get("help.browser_link"), commandler.getWebsite());
        builder.setDescription(scripts.get("help.channel_prefix"));

        builder.setDescription(String.join(", ", commandler.getContext().getGroups().keySet()));
        return builder;
    }

    @Command(id = "help.modules.name", aliases = "modules", help = "help.modules.help")
    @Param(id = "group", help = "The group to list modules for.")
    public Object modules(JDACEvent event, String group) {
        GenericMessageEvent source = event.getSource();
        var groups = commandler.getContext().getGroups(false);

        List<ModuleData> groupData = null;

        for (String in : groups.keySet()) {
            if (in.equalsIgnoreCase(group))
                groupData = groups.get(in);
        }

        if (groupData == null)
            return "That group doesn't exist.";

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(scripts.get("help.browser_link"), commandler.getWebsite());
        builder.setDescription(scripts.get("help.channel_prefix"));

        boolean disabled = false;

        Iterator<ModuleData> it = groupData.iterator();

        while (it.hasNext()) {
            ModuleData meta = it.next();

            if (!meta.isPublic())
                continue;

            Module module = meta.getAnnotation();
            String name = module.id();
            String[] aliases = module.aliases();
            String help = scripts.get(module.help());

//            if (!handler.isEnabled()) {
//                name = "~~" + name + "~~";
//
//                if (!disabled) {
//                    disabled = true;
//                    builder.appendDescription("\n" + BotUtils.getScript("help.module_disabled", source));
//                }
//            }

            StringJoiner joiner = new StringJoiner(", ");

            for (String alias : aliases)
                joiner.add("`" + alias + "`");

            Map<String, Object> aliasesMap = Map.of("aliases", aliases.length);
            help = "**" + BotUtils.getScript("help.aliases", source, aliasesMap) + ": **" + joiner.toString() + "\n" + help;

            if (it.hasNext())
                help += "\n_ _";

            builder.addField(scripts.get(name), help, false);
        }

        builder.appendDescription("\n_ _");
        builder.setFooter(scripts.get("help.example"), null);

        return builder;
    }


}
