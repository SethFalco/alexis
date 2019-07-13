package com.elypia.alexis.commandler.modules.media;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.*;
import com.elypia.elypiai.twitch.*;
import com.google.inject.Inject;
import org.slf4j.*;

@Module(name = "twitch", group = "Media", aliases = "twitch")
public class TwitchModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(TwitchModule.class);

    private LanguageInterface lang;
    private Twitch twitch;

    @Inject
    public TwitchModule(LanguageInterface lang, Twitch twitch) {
        this.lang = lang;
        this.twitch = twitch;
    }

    @Command(name = "info", aliases = {"get", "info"})
    public void getUser(CommandlerEvent<?> event, @Param(name = "username") String username) {
        TwitchQuery query = new TwitchQuery();
        query.addUsername(username);

        twitch.getUsers(query).queue(users -> {
//            if (users.isEmpty())
//                event.send("twitch.info.no_user", Map.of("username", username));
//            else
//                event.send(users.get(0));
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
