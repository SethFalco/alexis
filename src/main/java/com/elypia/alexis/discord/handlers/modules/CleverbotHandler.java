package com.elypia.alexis.discord.handlers.modules;

import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.impl.CommandHandler;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.elypiai.cleverbot.Cleverbot;
import com.elypia.jdautils.annotations.command.Command;
import com.elypia.jdautils.annotations.command.Module;
import com.elypia.jdautils.annotations.command.Param;
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

    @Command (aliases = {"say", "ask"}, help = "Say something to Cleverbot.")
    @Param(name = "body", help = "What you want to say.")
    public void say(MessageEvent event, String body) {
        String cs = getCs(event.getChannel());

        cleverbot.say(body, cs, response -> {
            event.reply(response.getOutput());
            setCs(event.getChannel(), response.getCS());
        }, failure -> BotUtils.httpFailure(event, failure));
    }

    @Command (aliases = {"history", "his"}, help = "Track previous conversation in this channel.")
    public void getHistory(MessageEvent event) {
        String cs = getCs(event.getChannel());
        String history = cleverbot.getHistory(cs);

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
