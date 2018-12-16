package com.elypia.alexis.modules.gaming;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.elypiai.osu.Osu;
import com.elypia.elypiai.osu.data.OsuMode;
import com.elypia.jdac.alias.*;
import org.slf4j.*;

import javax.validation.constraints.Size;

@Module(id = "osu.title", group = "Gaming", aliases = {"osu"}, help = "osu.help")
public class OsuModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(OsuModule.class);

    private Osu osu;

    public OsuModule() {
        osu = new Osu(Alexis.config.getApiCredentials().getOsu());
    }

    @Command(id = "Player Stats", aliases = "get", help = "Get stats on osu! players.")
    @Param(id = "players", help = "The players usernames you want to retrieve.")
    public void getPlayers(JDACEvent event, @Size(min = 3, max = 15) String username) {
        getPlayers(event, username, OsuMode.OSU);
    }

    @Overload("Player Stats")
    @Param(id = "mode", help = "The mode to use when getting players.")
    public void getPlayers(
        JDACEvent event,
        @Size(min = 3, max = 15) String username,
        OsuMode mode
    ) {
        osu.getPlayer(username, mode, 31).queue(player -> {
            event.send((player != null) ? player : "osu.player_not_found");
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
