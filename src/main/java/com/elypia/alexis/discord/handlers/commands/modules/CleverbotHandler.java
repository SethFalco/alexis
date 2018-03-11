package com.elypia.alexis.discord.handlers.commands.modules;

import com.elypia.alexis.discord.annotation.Command;
import com.elypia.alexis.discord.annotation.Module;
import com.elypia.alexis.discord.annotation.Parameter;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.cleverbot.Cleverbot;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.bson.Document;

import java.util.logging.Level;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Module(
    aliases = {"Cleverbot", "cb"},
    help = "Come talk to cleverbot."
)
public class CleverbotHandler extends CommandHandler {

    private Cleverbot cleverbot;
    private MongoDatabase database;
    private MongoCollection<Document> channels;

    public CleverbotHandler(MongoClient client) {
        database = client.getDatabase("alexis");
        channels = database.getCollection("channels");

        MongoDatabase global = client.getDatabase("global");
        MongoCollection<Document> api = global.getCollection("api_details");
        Document document = api.find(eq("service", "cleverbot")).first();

        cleverbot = new Cleverbot(document.getString("api_key"));
    }

    public CleverbotHandler(String apiKey) {
        cleverbot = new Cleverbot(apiKey);
    }

    @Command (
        aliases = {"say", "ask"},
        help = "Say something to Cleverbot.",
        params = {
            @Parameter(
                param = "body",
                help = "What you want to say.",
                type = String.class
            )
        }
    )
    public void say(MessageEvent event) {
        String body = event.getParams()[0];
        String cs = getCs(event.getChannel());

        cleverbot.say(body, cs, response -> {
            event.reply(response.getOutput());
            setCs(event.getChannel(), response.getCS());
        }, failure -> {
            BotUtils.unirestFailure(failure, event);
        });
    }

    @Command (
        aliases = {"history", "his"},
        help = "Track previous conversation in this channel."
    )
    public void getHistory(MessageEvent event) {
        String cs = getCs(event.getChannel());
        String history = cleverbot.getHistoryScript(cs);

        if (history == null)
            event.reply("Maybe try talk first and I can grab your history later?");
        else
            event.reply(String.format("```\n%s\n```", history));
    }

    private String getCs(MessageChannel channel) {
        long channelId = channel.getIdLong();
        Document document = channels.find(eq("channel_id", channelId)).first();

        if (document != null)
            return document.getString("cs");
        else
            return null;
    }

    private void setCs(MessageChannel channel, String cs) {
        long channelId = channel.getIdLong();

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);

        UpdateResult result = channels.updateOne(
            eq("channel_id",channelId),
            set("cs", cs),
            options
        );

        if (!result.wasAcknowledged())
            BotUtils.LOGGER.log(Level.SEVERE, "Cleverbot CS update was not acknowledged.");
    }
}
