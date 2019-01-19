package com.elypia.alexis.commandler.modules;

import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.AssignableRole;
import com.elypia.alexis.utils.ExitCode;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
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

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public DeveloperModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "dev.rename", aliases = "name", help = "dev.name.h")
    @Param(id = "dev.name.p.name", help = "dev.name.p.name.h")
    public void name(@Developer JDACEvent event, String input) {
        event.getSource().getJDA().getSelfUser().getManager().setName(input).queue(o -> {
            var params = Map.of("name", input);
            event.send("dev.name.success");
        }, failure -> event.send("dev.name.failure"));
    }

    @Command(id = "dev.avatar", aliases = {"avatar", "pp"}, help = "dev.avatar.h")
    @Param(id = "common.url", help = "dev.avatar.p.url.h")
    public void avatar(@Developer JDACEvent event, URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            Icon icon = Icon.from(stream);

            event.getSource().getJDA().getSelfUser().getManager().setAvatar(icon).queue(o -> {
                event.send("dev.avatar.success");
            }, failure -> event.send("dev.name.failure"));
        }
    }

    @Command(id = "dev.shutdown", aliases = "shutdown", help = "dev.shutdown.h")
    public void shutdown(@Developer JDACEvent event) {
        JDA jda = event.getSource().getJDA();
        jda.removeEventListener(jda.getRegisteredListeners());
        jda.shutdown();

        ExitCode code = ExitCode.PEACEFUL;

        logger.trace(code.getMessage());
        System.exit(code.getStatusCode());
    }

    @Command(id = "dev.embed", aliases = "embed", help = "dev.embed.h")
    @Param(id = "common.url", help = "dev.embed.p.url.h")
    public EmbedBuilder embedTest(@Developer String url) {
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
