package com.elypia.alexis.modules.gaming;

import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.interfaces.Handler;
import com.elypia.elypiai.osu.Osu;
import com.elypia.elypiai.osu.data.OsuMode;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.util.Objects;

@Module(name = "osu", group = "Gaming", aliases = {"osu"})
public class OsuModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(OsuModule.class);

    private final Osu osu;

    @Inject
    public OsuModule(Osu osu) {
        this.osu = Objects.requireNonNull(osu);
    }

    @Command(name = "osu.stats", aliases = "get")
    public void getPlayers(
        CommandlerEvent<MessageReceivedEvent> event,
        @Param(name = "p.players") @Size(min = 3, max = 15) String username,
        @Param(name = "p.mode") OsuMode mode
    ) {
        osu.getPlayer(username, mode).queue(player -> {
//            TODO: Make a way to send messages myself using the responseManager.
//            event.send((player != null) ? player : "osu.player_not_found");
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
