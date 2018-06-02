package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandHandler;
import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.Param;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.elypiai.cleverbot.Cleverbot;
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
    name = "Module",
    aliases = {"cleverbot", "cb"},
    description = "Come talk to cleverbot."
)
public class CleverbotHandler extends CommandHandler {

    private Cleverbot cleverbot;
    private MongoDatabase database;
    private MongoCollection<Document> channels;

    public CleverbotHandler(String apiKey) {
        cleverbot = new Cleverbot(apiKey);
    }

    @Command(aliases = {"say", "ask"}, help = "Say something to Cleverbot.")
    @Param(name = "body", help = "What you want to say.")
    public void say(MessageEvent event, String body) {
        MessageChannel channel = event.getMessageEvent().getChannel();
        String cs = getCs(channel);

        cleverbot.say(body, cs, response -> {
            event.reply(response.getOutput());
            setCs(channel, response.getCS());
        }, failure -> BotUtils.sendHttpError(event, failure));
    }

    @Command (aliases = {"history", "his"}, help = "Track previous conversation in this channel.")
    public void getHistory(MessageEvent event) {
        String cs = getCs(event.getMessageEvent().getChannel());
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
            BotUtils.log(Level.SEVERE, "Cleverbot CS update was not acknowledged.");
    }
}
