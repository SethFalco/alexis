package com.elypia.alexis.commandler.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.embedded.AmazonCredentials;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.elypiai.amazon.Amazon;
import com.elypia.jdac.alias.*;
import org.slf4j.*;

import java.security.InvalidKeyException;

@Module(id = "Amazon", aliases = "amazon", help = "amazon.help")
public class AmazonModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(AmazonModule.class);

    private Amazon amazon;

    public AmazonModule() {
        AmazonCredentials detail = Alexis.config.getApiCredentials().getAmazonCredentials().get(0);

        try {
            amazon = new Amazon(detail.getKey(), detail.getSecret(), detail.getTag(), detail.getEndpoint());
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            enabled = false;
        }
    }

    @Default
    @Command(id = "amazon.search.name", aliases = {"search", "get"}, help = "amazon.search.help")
    @Param(id = "common.query", help = "amazon.param.query.help")
    public void getItem(JDACEvent event, String query) {
        amazon.getItems(query).queue((result) -> {
            if (result != null)
                event.send(result.getItems().get(0));
            else
                event.send("amazon.no_results");
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
