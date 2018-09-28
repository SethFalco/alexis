package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.embedded.AmazonCredentials;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.amazon.Amazon;
import org.slf4j.*;

import java.security.InvalidKeyException;

@Module(name = "amazon.title", aliases = "amazon", help = "amazon.help")
public class AmazonHandler extends JDAHandler {

    private static final Logger logger = LoggerFactory.getLogger(AmazonHandler.class);

    private Amazon amazon;

    public AmazonHandler() {
        AmazonCredentials detail = Alexis.config.getApiCredentials().getAmazonCredentials().get(0);

        try {
            amazon = new Amazon(detail.getKey(), detail.getSecret(), detail.getTag(), detail.getEndpoint());
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            enabled = false;
        }
    }

    @Default
    @Command(name = "amazon.search.name", aliases = {"search", "get"}, help = "amazon.search.help")
    @Param(name = "common.query", help = "amazon.param.query.help")
    public void getItem(JDACommand event, String query) {
        amazon.getItems(query).queue((result) -> {
            if (result != null)
                event.reply(result.getItems().get(0));
            else
                event.reply(BotUtils.getScript("amazon.no_results", event.getSource()));
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
