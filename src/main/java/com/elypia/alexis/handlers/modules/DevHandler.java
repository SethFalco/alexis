package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.utils.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Icon;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

@Developer
@Module(name = "Developer Only Commands", aliases = "dev")
public class DevHandler extends JDAHandler {

    @Command(name = "Update Global Name", aliases = "name", help = "help.name")
    @Param(name = "input", help = "help.name.input")
    public void name(JDACommand event, String input) {
        client.getSelfUser().getManager().setName(input).queue(o -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("name", input);

            event.reply(BotUtils.getScript("name.response", event.getSource()));
        });
    }

    @Command(name = "Update Avatar", aliases = {"avatar", "pp"}, help = "help.avatar")
    @Param(name = "url", help = "help.avatar.url")
    public void avatar(JDACommand event, URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);

            client.getSelfUser().getManager().setAvatar(icon).queue(o -> {
                event.reply("avatar.response");
            });
        }
    }

    @Command(name = "Shutdown", aliases = "shutdown", help = "help.shutdown")
    public void shutdown(JDACommand event) {
        client.removeEventListener(client.getRegisteredListeners());
        client.shutdown();

        ExitCode code = ExitCode.PEACEFUL;
        DiscordLogger.log(event, Level.ALL, code.getMessage());
        System.exit(code.getStatusCode());
    }

    @Command(name = "List all Guilds", aliases = "guilds", help = "help.guilds")
    public String getGuilds() {
        StringJoiner joiner = new StringJoiner("\n");
        client.getGuilds().forEach(o -> joiner.add(o.getName()));
        return joiner.toString();
    }

    @Command(name = "Embed Test", aliases = "embed", help = "help.embed")
    @Param(name = "url", help = "help.embed.url")
    public EmbedBuilder embedTest(String url) {
        return new EmbedBuilder().setImage(url);
    }
}
