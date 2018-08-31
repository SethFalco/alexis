package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.utils.DiscordLogger;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.osu.Osu;
import com.elypia.elypiai.osu.data.OsuMode;
import com.elypia.elyscript.ElyScript;

@Module(name = "osu!", aliases = {"osu"}, help = "Integration with osu!, the popular rhythem game.")
public class OsuHandler extends JDAHandler {

    private static final ElyScript PLAYER_NOT_FOUND = new ElyScript("(Sorry|I apologise)(,|...) I ((did|could)(n't| not)|was unable to) find the( osu!){?} (user|player) you specified.( :c){?}");

    private Osu osu;

    public OsuHandler() {
        osu = new Osu(Alexis.config.getApiKeys().getOsu());
    }

    @Command(id = 10, name = "Player Stats", aliases = "get", help = "Get stats on osu! players.")
    @Param(name = "players", help = "The players usernames you want to retrieve.")
    public void getPlayers(JDACommand event, @Length(min = 3, max = 15) String username) {
        getPlayers(event, username, OsuMode.OSU);
    }

    @Overload(10)
    @Param(name = "mode", help = "The mode to use when getting players.")
    public void getPlayers(JDACommand event, String username, OsuMode mode) {
        osu.getPlayer(username, mode, 31).queue(player -> {
            event.reply(player != null ? player : PLAYER_NOT_FOUND.compile());
        }, (ex) -> DiscordLogger.log(event, ex));
    }
}
