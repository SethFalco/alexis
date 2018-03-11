package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.events.CommandEvent;
import com.elypia.alexis.discord.handlers.commands.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.amazon.Amazon;
import com.elypia.elypiai.amazon.AmazonItem;
import com.elypia.elypiai.amazon.data.AmazonEndpoint;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.EmbedBuilder;
import org.bson.Document;

import java.security.InvalidKeyException;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

@Module (
    aliases = "Amazon",
    help = "Share links and support Elypia! We get a cut from purchases!"
)
public class AmazonHandler extends CommandHandler {

    private Amazon amazon;

    public AmazonHandler(MongoCollection<Document> apiDetails, AmazonEndpoint endpoint) {
        this(apiDetails.find(eq("service", "amazon")).first(), endpoint);
    }

    public AmazonHandler(Document document, AmazonEndpoint endpoint) {
        this (
            document.getString("access_key"),
            document.getString("secret"),
            document.getString("us"),
            endpoint
        );
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

    @Command (
        aliases = {"search", "get", "gimme"},
        help = "Search Amazon for a product and share it.",
        params = {
            @Parameter (
                param = "product",
                help = "Name of the product you're after.",
                type = String.class
            )
        }
    )
    public void getItem(CommandEvent event) {
        String query = event.getParams()[0];

        amazon.getItem(query, result -> {
            AmazonItem item = result.get(0);

            EmbedBuilder builder = new EmbedBuilder();

            builder.setThumbnail(item.getLargeImage());
            builder.setDescription(item.getUrl());
            builder.addField("Price", item.getPricePretty(), false);
            builder.setFooter(String.format("This is for %s", item.getAmazon().getEndpoint().getShoppingUrl()), null);

            event.reply(builder);
        }, failure -> {
            BotUtils.unirestFailure(failure, event);
        });
    }
}
