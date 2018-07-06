package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.validation.param.Length;
import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.osu.Osu;
import com.elypia.elypiai.osu.data.OsuMode;
import com.elypia.elypiai.utils.elyscript.ElyScript;

@Module(name = "osu!", aliases = {"osu"}, description = "Integration with osu!, the popular rhythem game.")
public class OsuHandler extends CommandHandler {

    private static final ElyScript PLAYER_NOT_FOUND = new ElyScript("(Sorry|I apologise)(,|...) I ((did|could)(n't| not)|was unable to) find the( osu!){?} (user|player) you specified.( :c){?}");

    private Osu osu;

    public OsuHandler(String apikey) {
        osu = new Osu(apikey);
    }

    @Command(id = 10, name = "Player Stats", aliases = "get", help = "Get stats on osu! players.")
    @Param(name = "players", help = "The players usernames you want to retrieve.")
    public void getPlayers(AbstractEvent event, @Length(min = 3, max = 15) String username) {
        getPlayers(event, username, OsuMode.OSU);
    }

    @Overload(10)
    @Param(name = "mode", help = "The mode to use when getting players.")
    public void getPlayers(AbstractEvent event, String username, OsuMode mode) {
        osu.getPlayer(username, mode, 31).queue(player -> {
            event.reply(player != null ? player : PLAYER_NOT_FOUND.compile());
        }, (ex) -> BotUtils.sendHttpError(event, ex));
    }
}
