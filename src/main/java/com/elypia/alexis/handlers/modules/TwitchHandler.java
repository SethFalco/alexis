package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.utils.DiscordLogger;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.twitch.*;

@Module(name = "Twitch", aliases = "twitch", help = "Get information on various streamers!")
public class TwitchHandler extends JDAHandler {

    private Twitch twitch;

    public TwitchHandler() {
        twitch = new Twitch(Alexis.config.getApiKeys().getTwitch());
    }

    @Command(name = "Streamer Info", aliases = {"get", "info"}, help = "Get information on streamers.")
    @Param(name = "usernames", help = "The user to retrieve.")
    public void getUser(JDACommand event, String username) {
        TwitchQuery query = new TwitchQuery();
        query.addUsername(username);

        twitch.getUsers(query).queue(users -> {
            event.reply(!users.isEmpty() ? users.get(0) : "Sorry I couldn't find " + username + ".");
        }, failure -> DiscordLogger.log(event, failure));
    }
}
