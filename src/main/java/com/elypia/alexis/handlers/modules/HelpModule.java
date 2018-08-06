package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandEvent;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.impl.IHandler;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.metadata.MetaModule;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.util.*;

// ! For not this is in Alexis, we should come up with a generic help module for Commandler though.
@Module(name = "Help", aliases = "help")
public class HelpModule extends JDAHandler {

    @Override
    @Default
    @Command(id = 1, name = "Help", aliases = {"help", "halp", "helpme"})
    public Object help(JDACommand event) {
        return super.help(event);
    }

    @Overload(1)
    @Param(name = "module", help = "help.help.module")
    public String help(JDACommand event, String module) {
        if (commandler.getRoots().containsKey(module)) {
            MetaModule metaModule = commandler.getRoots().get(module);

            if (commandler.getRoots().get(module).performed(module)) {
                event.trigger(metaModule.getAliases().get(0) + " help");
                return null;
            }
        }

        return "I couldn't find a command under that name.";
    }

    @Command(name = "List all Modules", aliases = "modules", help = "help.modules")
    public EmbedBuilder modules(JDACommand event) {
        GenericMessageEvent source = event.getSource();
        String prefix = confiler.getPrefixes(source)[0];
        Collection<IHandler<JDA, GenericMessageEvent, Message>> handlers = commandler.getHandlers();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(BotUtils.getScript("modules.title", source), confiler.getHelpUrl(event.getSource()));
        builder.setDescription(BotUtils.getScript("modules.description.prefix", source));

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
            String help = module.help();

            if (!handler.isEnabled()) {
                name = "~~" + name + "~~";

                if (!disabled) {
                    disabled = true;
                    builder.appendDescription("\n" + BotUtils.getScript("modules.description.disabled", source));
                }
            }

            StringJoiner joiner = new StringJoiner(", ");

            for (String alias : aliases)
                joiner.add("`" + alias + "`");

            help = "**" + BotUtils.getScript("modules.field.aliases", source) + ": **" + joiner.toString() + "\n" + help;

            if (it.hasNext())
                help += "\n_ _";

            builder.addField(name, help, false);
        }

        builder.appendDescription("\n_ _");
        builder.setFooter("Try \"" + prefix + "{module} help\" for how to perform commands!", null);

        return builder;
    }
}
