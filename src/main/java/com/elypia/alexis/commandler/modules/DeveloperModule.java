package com.elypia.alexis.commandler.modules;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.AssignableRole;
import com.elypia.alexis.utils.ExitCode;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.jdac.*;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.Developer;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.slf4j.*;

import java.io.*;
import java.net.URL;
import java.util.*;

@Module(id = "Developer", aliases = "dev")
public class DeveloperModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(DeveloperModule.class);

    @Command(id = "dev.rename", aliases = "name", help = "dev.name.h")
    public void name(
        @Developer JDACEvent event,
        @Param(id = "dev.name.p.name", help = "dev.name.p.name.h") String input
    ) {
        event.getSource().getJDA().getSelfUser().getManager().setName(input).queue(o -> {
            var params = Map.of("name", input);
            event.send("dev.name.success");
        }, failure -> event.send("dev.name.failure"));
    }

    @Command(id = "dev.avatar", aliases = {"avatar", "pp"}, help = "dev.avatar.h")
    public void avatar(
        @Developer JDACEvent event,
        @Param(id = "common.url", help = "dev.avatar.p.url.h") URL url
    ) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);

            event.getSource().getJDA().getSelfUser().getManager().setAvatar(icon).queue(o -> {
                event.send("dev.avatar.success");
            }, failure -> event.send("dev.name.failure"));
        }
    }

    public void shutdownCommand(@Developer JDACEvent event) {
        JDA jda = event.getSource().getJDA();
        jda.removeEventListener(jda.getRegisteredListeners());
        jda.shutdown();

        ExitCode code = ExitCode.PEACEFUL;

        logger.trace(code.getMessage());
        System.exit(code.getStatusCode());
    }

    @Command(id = "dev.embed", aliases = "embed", help = "dev.embed.h")
    public EmbedBuilder embedTest(
        @Developer JDACEvent event,
        @Param(id = "common.url", help = "dev.embed.p.url.h") String url
    ) {
        return new EmbedBuilder().setImage(url);
    }

    @Command(id = "dev.clean", aliases = "clean")
    public String clean(@Developer JDACEvent event) {
        event.getSource().getJDA().getGuilds().forEach((guild) -> {
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
