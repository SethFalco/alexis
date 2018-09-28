package com.elypia.alexis.handlers.modules.media;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.twitch.*;
import org.slf4j.*;

import java.util.Map;

@Module(name = "twitch", group = "Media", aliases = "twitch", help = "twitch.h")
public class TwitchHandler extends JDAHandler {

    private static final Logger logger = LoggerFactory.getLogger(TwitchHandler.class);

    private Twitch twitch;

    public TwitchHandler() {
        twitch = new Twitch(Alexis.config.getApiCredentials().getTwitch());
    }

    @Command(name = "twitch.info", aliases = {"get", "info"}, help = "twitch.info.h")
    @Param(name = "common.username", help = "twitch.info.p.user.h")
    public void getUser(JDACommand event, String username) {
        TwitchQuery query = new TwitchQuery();
        query.addUsername(username);

        twitch.getUsers(query).queue(users -> {
            if (users.isEmpty())
                event.replyScript("twitch.info.no_user", Map.of("username", username));
            else
                event.reply(users.get(0));
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
