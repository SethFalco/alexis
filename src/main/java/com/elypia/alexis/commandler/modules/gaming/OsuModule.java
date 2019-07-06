package com.elypia.alexis.commandler.modules.gaming;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.commandler.managers.ResponseManager;
import com.elypia.elypiai.osu.Osu;
import com.elypia.elypiai.osu.data.OsuMode;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.util.Objects;

@Module(name = "osu!", group = "Gaming", aliases = {"osu"}, help = "osu.help")
public class OsuModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(OsuModule.class);

    private final Osu osu;
    private final ResponseManager responseManager;

    @Inject
    public OsuModule(Osu osu, ResponseManager responseManager) {
        this.osu = Objects.requireNonNull(osu);
        this.responseManager = Objects.requireNonNull(responseManager);
    }

    @Command(name = "Player Stats", aliases = "get", help = "Get stats on osu! players.")
    public void getPlayers(
        CommandlerEvent<MessageReceivedEvent> event,
        @Param(name = "players", help = "The players usernames you want to retrieve.") @Size(min = 3, max = 15) String username,
        @Param(name = "mode", help = "The mode to use when getting players.") OsuMode mode
    ) {
        osu.getPlayer(username, mode, 31).queue(player -> {
            event.send((player != null) ? player : "osu.player_not_found");
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
