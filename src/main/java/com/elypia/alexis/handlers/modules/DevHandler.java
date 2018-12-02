package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.AssignableRole;
import com.elypia.alexis.utils.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Icon;
import org.slf4j.*;

import java.io.*;
import java.net.URL;
import java.util.*;

//@Developer
@Module(name = "dev", aliases = "dev")
public class DevHandler extends JDAHandler {

    private static final Logger logger = LoggerFactory.getLogger(DevHandler.class);

    @Command(name = "dev.rename", aliases = "name", help = "dev.name.h")
    @Param(name = "dev.name.p.name", help = "dev.name.p.name.h")
    public void name(JDACommand event, String input) {
        client.getSelfUser().getManager().setName(input).queue(o -> {
            var params = Map.of("name", input);
            event.reply(BotUtils.getScript("dev.name.success", event.getSource()));
        }, failure -> event.replyScript("dev.name.failure"));
    }

    @Command(name = "dev.avatar", aliases = {"avatar", "pp"}, help = "dev.avatar.h")
    @Param(name = "common.url", help = "dev.avatar.p.url.h")
    public void avatar(JDACommand event, URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);

            client.getSelfUser().getManager().setAvatar(icon).queue(o -> {
                event.replyScript("dev.avatar.success");
            }, failure -> event.replyScript("dev.name.failure"));
        }
    }

    @Command(name = "dev.shutdown", aliases = "shutdown", help = "dev.shutdown.h")
    public void shutdown(JDACommand event) {
        client.removeEventListener(client.getRegisteredListeners());
        client.shutdown();

        ExitCode code = ExitCode.PEACEFUL;

        logger.trace(code.getMessage());
        System.exit(code.getStatusCode());
    }

    @Command(name = "dev.embed", aliases = "embed", help = "dev.embed.h")
    @Param(name = "common.url", help = "dev.embed.p.url.h")
    public EmbedBuilder embedTest(String url) {
        return new EmbedBuilder().setImage(url);
    }

    @Command(name = "dev.clean", aliases = "clean")
    public String clean() {
        client.getGuilds().forEach((guild) -> {
            GuildData data = GuildData.query(guild);
            List<AssignableRole> roles = data.getSettings().getAssignableRoles();
            int originalSize = roles.size();

            for (int i = roles.size() - 1; i >= 0; i--) {
                if (guild.getRoleById(roles.get(i).getRoleId()) == null)
                    roles.remove(i);
            }

            if (originalSize != roles.size())
                data.commit();
        });

        return "Done!";
    }
}
