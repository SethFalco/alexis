package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.AmazonDetails;
import com.elypia.alexis.utils.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.amazon.Amazon;
import com.elypia.elyscript.ElyScriptStore;

import java.security.InvalidKeyException;
import java.util.*;

@Module(name = "Amazon", aliases = "amazon", help = "amazon.help")
public class AmazonHandler extends JDAHandler {

    private Amazon amazon;

    private ElyScriptStore scripts;

    public AmazonHandler() {
        AmazonDetails detail = Alexis.config.getApiKeys().getAmazonDetails().get(0);

        try {
            amazon = new Amazon(detail.getKey(), detail.getSecret(), detail.getTag(), detail.getEndpoint());
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            enabled = false;
        }
    }

    @Default
    @Command(name = "amazon.search.title", aliases = {"search", "get"}, help = "amazon.search.help")
    @Param(name = "amazon.param.query.name", help = "amazon.param.query.help")
    public void getItem(JDACommand event, String query) {
        amazon.getItems(query).queue((result) -> {
            if (result != null)
                event.reply(result.getItems().get(0));
            else
                event.reply(BotUtils.getScript("amazon.no_results", event.getSource()));
        }, (failure) -> DiscordLogger.log(event, failure));
    }
}
