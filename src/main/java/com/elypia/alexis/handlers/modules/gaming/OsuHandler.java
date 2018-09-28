package com.elypia.alexis.handlers.modules.gaming;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.osu.Osu;
import com.elypia.elypiai.osu.data.OsuMode;
import org.slf4j.*;

@Module(name = "osu.title", group = "Gaming", aliases = {"osu"}, help = "osu.help")
public class OsuHandler extends JDAHandler {

    private static final Logger logger = LoggerFactory.getLogger(OsuHandler.class);

    private Osu osu;

    public OsuHandler() {
        osu = new Osu(Alexis.config.getApiCredentials().getOsu());
    }

    @Command(id = 10, name = "osu.get.title", aliases = "get", help = "Get stats on osu! players.")
    @Param(name = "players", help = "The players usernames you want to retrieve.")
    public void getPlayers(JDACommand event, @Length(min = 3, max = 15) String username) {
        getPlayers(event, username, OsuMode.OSU);
    }

    @Overload(10)
    @Param(name = "mode", help = "The mode to use when getting players.")
    public void getPlayers(JDACommand event, String username, OsuMode mode) {
        osu.getPlayer(username, mode, 31).queue(player -> {
            if (player != null)
                event.reply(player);
            else
                event.replyScript("osu.player_not_found");
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
