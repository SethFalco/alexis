package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.impl.IHandler;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.metadata.MetaModule;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;
import java.util.stream.Collectors;

@Module(name = "help.title", aliases = "help")
public class HelpModule extends JDAHandler {

    @Override
    @Default
    @Command(id = 1, name = "help.modules.title", aliases = {"help", "halp", "helpme"})
    public Object help(JDACommand event) {
        return super.help(event);
    }

    @Overload(1)
    @Param(name = "help.param.module.name", help = "help.param.module.help")
    public void help(JDACommand event, String module) {
        if (commandler.getRoots().containsKey(module)) {
            MetaModule metaModule = commandler.getRoots().get(module);

            if (commandler.getRoots().get(module).performed(module)) {
                event.trigger(metaModule.getAliases().get(0) + " help");
                return;
            }
        }

        event.replyScript("help.module_not_found");
    }

    @Command(id = 600, name = "Groups", aliases = {"group", "groups"}, help = "Display a list of all module groups.")
    public EmbedBuilder listGroups(JDACommand event) {
        GenericMessageEvent source = event.getSource();
        Collection<IHandler<JDA, GenericMessageEvent, Message>> handlers = commandler.getHandlers();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(event.getScript("help.browser_link"), confiler.getHelpUrl(event.getSource()));
        builder.setDescription(event.getScript("help.channel_prefix"));

        builder.setDescription(String.join(", ", commandler.getGroups()));
        return builder;
    }

    @Command(name = "help.modules.name", aliases = "modules", help = "help.modules.help")
    @Param(name = "group", help = "The group to list modules for.")
    public Object modules(JDACommand event, String group) {
        GenericMessageEvent source = event.getSource();
        Collection<IHandler<JDA, GenericMessageEvent, Message>> handlers = commandler.getHandlers();
        handlers = handlers.stream().filter(handler -> {
            return handler.getModule().getModule().group().toLowerCase().equals(group.toLowerCase());
        }).collect(Collectors.toList());

        if (handlers.isEmpty())
            return "That group doesn't exist.";

        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(event.getScript("help.browser_link"), confiler.getHelpUrl(event.getSource()));
        builder.setDescription(event.getScript("help.channel_prefix"));

        boolean disabled = false;

        Iterator<IHandler<JDA, GenericMessageEvent, Message>> it = handlers.iterator();

        while (it.hasNext()) {
            IHandler<JDA, GenericMessageEvent, Message> handler = it.next();
            MetaModule meta = handler.getModule();

            if (!meta.isPublic())
                continue;

            Module module = meta.getModule();
            String name = module.name();
            String[] aliases = module.aliases();
            String help = event.getScript(module.help());

            if (!handler.isEnabled()) {
                name = "~~" + name + "~~";

                if (!disabled) {
                    disabled = true;
                    builder.appendDescription("\n" + BotUtils.getScript("help.module_disabled", source));
                }
            }

            StringJoiner joiner = new StringJoiner(", ");

            for (String alias : aliases)
                joiner.add("`" + alias + "`");

            Map<String, Object> aliasesMap = Map.of("aliases", aliases.length);
            help = "**" + BotUtils.getScript("help.aliases", source, aliasesMap) + ": **" + joiner.toString() + "\n" + help;

            if (it.hasNext())
                help += "\n_ _";

            builder.addField(event.getScript(name), help, false);
        }

        builder.appendDescription("\n_ _");
        builder.setFooter(event.getScript("help.example"), null);

        return builder;
    }


}
