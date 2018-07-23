package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.*;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Icon;

import java.io.*;
import java.net.URL;
import java.util.StringJoiner;
import java.util.logging.Level;

//@Developer // ! Removed this for now
@Module(name = "Developer Only Commands", aliases = "dev")
public class DevHandler extends JDAHandler {

    @Command(name = "Update Global Name", aliases = "name", help = "Change the global name of the chatbot.")
    @Param(name = "input", help = "The new name to set the chatbot too.")
    public void name(JDACommand event, String input) {
        client.getSelfUser().getManager().setName(input).queue(o -> {
            event.reply("Name has succesfully been changed to " + input + ".");
        });
    }

    @Command(name = "Update Avatar", aliases = {"avatar", "pp"}, help = "Change the avatar of the chatbot.")
    @Param(name = "url", help = "Location for the new avatar for the chatbot.")
    public void avatar(JDACommand event, URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);

            client.getSelfUser().getManager().setAvatar(icon).queue(o -> {
                event.reply("My avatar has succesfully been changed! Go check it out!");
            });
        }
    }

    @Command(name = "Shutdown", aliases = "shutdown", help = "Shutdown Alexis peacefully.")
    public void shutdown(JDACommand event) {
        client.removeEventListener(client.getRegisteredListeners());
        client.shutdown();

        ExitCode code = ExitCode.PEACEFUL;
        BotLogger.log(event, Level.ALL, code.getMessage());
        System.exit(code.getStatusCode());
    }

    @Command(name = "List all Guilds", aliases = "guilds", help = "List all the guilds the chatbot is in.")
    public String getGuilds() {
        StringJoiner joiner = new StringJoiner("\n");
        client.getGuilds().forEach(o -> joiner.add(o.getName()));
        return joiner.toString();
    }

    @Command(name = "Embed Test", aliases = "embed", help = "Specify a url and we'll see the result on embedding it.")
    @Param(name = "url", help = "The url to the image you want to embed.")
    public EmbedBuilder embedTest(String url) {
        return new EmbedBuilder().setImage(url);
    }
}
