package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.config.AmazonDetails;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.amazon.*;
import net.dv8tion.jda.core.EmbedBuilder;

import java.security.InvalidKeyException;
import java.util.*;

@Module(name = "Amazon", aliases = "amazon", description = "Share links and support Elypia! We get a cut from purchases!")
public class AmazonHandler extends CommandHandler {

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
    @Command(name = "Search Amazon", aliases = {"search", "get"}, help = "Search Amazon for a product and share it.")
    @Param(name = "query", help = "Name of the product you're after.")
    public void getItem(MessageEvent event, String query) {
        amazon.getItems(query, result -> {
            if (result.isEmpty()) {
                event.reply("Sorry, Amazon returned no results. :c");
                return;
            }

            AmazonItem item = result.get(0);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(item.getTitle(), item.getUrl());
            builder.setThumbnail(item.getImage());
            builder.addField("Price", item.getPriceString(), false);
            builder.setFooter("We get income from purchased under out Amazon links! ^-^", null);

            event.reply(builder);
        }, failure -> BotUtils.sendHttpError(event, failure));
    }
}
