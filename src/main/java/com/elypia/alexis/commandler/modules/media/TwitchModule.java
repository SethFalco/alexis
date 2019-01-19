package com.elypia.alexis.commandler.modules.media;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.elypiai.twitch.*;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.slf4j.*;

import java.util.Map;

@Module(id = "Twitch", group = "Media", aliases = "twitch", help = "twitch.h")
public class TwitchModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(TwitchModule.class);

    private Twitch twitch;

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public TwitchModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
        twitch = new Twitch(Alexis.config.getApiCredentials().getTwitch());
    }

    @Command(id = "twitch.info", aliases = {"get", "info"}, help = "twitch.info.h")
    @Param(id = "common.username", help = "twitch.info.p.user.h")
    public void getUser(JDACEvent event, String username) {
        TwitchQuery query = new TwitchQuery();
        query.addUsername(username);

        twitch.getUsers(query).queue(users -> {
            if (users.isEmpty())
                event.send("twitch.info.no_user", Map.of("username", username));
            else
                event.send(users.get(0));
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
