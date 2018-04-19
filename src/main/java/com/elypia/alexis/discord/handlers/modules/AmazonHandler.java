package com.elypia.alexis.discord.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.jda.events.MessageEvent;
import com.elypia.elypiai.amazon.*;
import com.elypia.elypiai.amazon.data.AmazonEndpoint;
import net.dv8tion.jda.core.EmbedBuilder;
import org.json.*;

import java.security.InvalidKeyException;
import java.util.*;

@Module(aliases = "Amazon", help = "Share links and support Elypia! We get a cut from purchases!")
public class AmazonHandler extends CommandHandler {

    private Amazon amazon;

    public AmazonHandler(JSONObject object) {
        Objects.requireNonNull(object);

        String accessKey = object.getString("access_key");
        String secret = object.getString("secret");

        JSONArray array = object.getJSONArray("stores");
        JSONObject store = array.getJSONObject(0);

        String id = store.getString("id");
        AmazonEndpoint endpoint = AmazonEndpoint.US; // Temp

        try {
            amazon = new Amazon(accessKey, secret, id, endpoint);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            enabled = false;
        }
    }

    public AmazonHandler(String accessKey, String secret, String id, AmazonEndpoint endpoint) {
        Objects.requireNonNull(accessKey);
        Objects.requireNonNull(secret);
        Objects.requireNonNull(id);
        Objects.requireNonNull(endpoint);

        try {
            amazon = new Amazon(accessKey, secret, id, endpoint);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            enabled = false;
        }
    }

    @Command(aliases = {"search", "get"}, help = "Search Amazon for a product and share it.")
    @Param(name = "query", help = "Name of the product you're after.")
    public void getItem(MessageEvent event, String query) {
        amazon.getItems(query, result -> {
            AmazonItem item = result.get(0);

            EmbedBuilder builder = new EmbedBuilder();

            builder.setThumbnail(item.getImage());
            builder.setDescription(item.getUrl());
            builder.addField("Price", item.getPriceString(), false);
            builder.setFooter(String.format("This is for %s", item.getAmazon().getEndpoint().getShoppingUrl()), null);

            event.reply(builder);
        }, failure -> BotUtils.httpFailure(event, failure));
    }
}
