package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.Developer;
import com.elypia.alexis.discord.annotations.Module;
import com.elypia.alexis.discord.annotations.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.utils.ExitCode;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Icon;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.StringJoiner;
import java.util.logging.Level;

import static com.elypia.alexis.utils.BotUtils.log;

@Module(
    aliases = "dev",
    help = "Only the developer(s) can perform these."
)
@Developer
public class DevHandler extends CommandHandler {

    @Command(aliases = "name", help = "Change the global name of the chatbot.")
    @Parameter(name = "input", help = "The new name to set the chatbot too.")
    public void name(MessageEvent event, String input) {
        event.getJDA().getSelfUser().getManager().setName(input).queue(o -> {
            event.reply("Name has succesfully been changed to " + input + ".");
        });
    }

    @Command(aliases = "avatar", help = "Change the avatar of the chatbot.")
    @Parameter(name = "url", help = "Location for the new avatar for the chatbot.")
    public void avatar(MessageEvent event, URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);

            event.getJDA().getSelfUser().getManager().setAvatar(icon).queue(o -> {
                event.reply("My avatar has succesfully been changed! Go check it out!");
            });
        }
    }

    @Command(aliases = "shutdown", help = "Shutdown Alexis peacefully.")
    public void shutdown(MessageEvent event) {
        JDA jda = event.getJDA();
        jda.removeEventListener(jda.getRegisteredListeners());
        jda.shutdown();

        ExitCode code = ExitCode.PEACEFUL;
        log(Level.INFO, code.getMessage());
        System.exit(code.getStatusCode());
    }

    @Command(aliases = "guilds", help = "List all the guilds the chatbot is in.")
    public void getGuilds(MessageEvent event) {
        StringJoiner joiner = new StringJoiner("\n");
        event.getJDA().getGuilds().forEach(o -> joiner.add(o.getName()));
        event.reply(joiner.toString());
    }
}
