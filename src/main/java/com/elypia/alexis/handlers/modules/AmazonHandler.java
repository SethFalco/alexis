package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.config.AmazonDetails;
import com.elypia.alexis.utils.*;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.elypiai.amazon.Amazon;

import java.security.InvalidKeyException;
import java.util.*;

@Module(name = "Amazon", aliases = "amazon", help = "help.amazon")
public class AmazonHandler extends JDAHandler {

    private Amazon amazon;

    public AmazonHandler(List<AmazonDetails> details) {
        Objects.requireNonNull(details);

        AmazonDetails detail = details.get(0);

        try {
            amazon = new Amazon(detail.getKey(), detail.getSecret(), detail.getTag(), detail.getEndpoint());
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            enabled = false;
        }
    }

    @Default
    @Command(name = "Search Amazon", aliases = {"search", "get"}, help = "help.amazon.search")
    @Param(name = "query", help = "help.amazon.search.query")
    public void getItem(JDACommand event, String query) {
        amazon.getItems(query).queue((result) -> {
            if (result != null)
                event.reply(result.getItems().get(0));
            else
                event.reply(BotUtils.getScript("amazon.no_results", event.getSource()));
        }, (failure) -> BotLogger.log(event, failure));
    }
}
