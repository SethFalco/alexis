package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.ExitCode;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Icon;

import java.io.*;
import java.net.URL;
import java.util.StringJoiner;
import java.util.logging.Level;

import static com.elypia.alexis.utils.BotUtils.log;

//@Developer
@Module(name = "Developer Only Commands", aliases = "dev", description = "Developer only commands", hidden = true)
public class DevHandler extends CommandHandler {

    @Command(name = "Update Global Name", aliases = "name", help = "Change the global name of the chatbot.")
    @Param(name = "input", help = "The new name to set the chatbot too.")
    public void name(MessageEvent event, String input) {
        event.getMessageEvent().getJDA().getSelfUser().getManager().setName(input).queue(o -> {
            event.reply("Name has succesfully been changed to " + input + ".");
        });
    }

    @Command(name = "Update Avatar", aliases = {"avatar", "pp"}, help = "Change the avatar of the chatbot.")
    @Param(name = "url", help = "Location for the new avatar for the chatbot.")
    public void avatar(MessageEvent event, URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);

            event.getMessageEvent().getJDA().getSelfUser().getManager().setAvatar(icon).queue(o -> {
                event.reply("My avatar has succesfully been changed! Go check it out!");
            });
        }
    }

    @Command(name = "Shutdown", aliases = "shutdown", help = "Shutdown Alexis peacefully.")
    public void shutdown(MessageEvent event) {
        JDA jda = event.getMessageEvent().getJDA();
        jda.removeEventListener(jda.getRegisteredListeners());
        jda.shutdown();

        ExitCode code = ExitCode.PEACEFUL;
        log(Level.INFO, code.getMessage());
        System.exit(code.getStatusCode());
    }

    @Command(name = "List all Guilds", aliases = "guilds", help = "List all the guilds the chatbot is in.")
    public String getGuilds(MessageEvent event) {
        StringJoiner joiner = new StringJoiner("\n");
        event.getMessageEvent().getJDA().getGuilds().forEach(o -> joiner.add(o.getName()));
        return joiner.toString();
    }
}
