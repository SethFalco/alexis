package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.commandler.validators.Database;
import com.elypia.alexis.entities.MessageChannelData;
import com.elypia.alexis.utils.BotLogger;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.elypiai.cleverbot.Cleverbot;
import net.dv8tion.jda.core.entities.MessageChannel;

// ! Make a way for users to add tweaks to Cleverbot
@Database
@Module(name = "CleverBot", aliases = {"cleverbot", "cb"}, help = "Come talk to cleverbot.")
public class CleverbotHandler extends JDAHandler {

    private Cleverbot cleverbot;

    public CleverbotHandler(String apiKey) {
        cleverbot = new Cleverbot(apiKey);
    }

    @Command(name = "Talk to Cleverbot", aliases = {"say", "ask"}, help = "Say something to Cleverbot.")
    @Param(name = "body", help = "What you want to say.")
    public void say(JDACommand event, String body) {
        MessageChannel channel = event.getSource().getChannel();
        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
        String cs = data.getCleverState();

        cleverbot.say(body, cs).queue(response -> {
            event.reply(response.getOutput());

            data.setCleverState(response.getCs());
            data.commit();
        }, failure -> BotLogger.log(event, failure));
    }

// ! This is disabled for now until we have caching available
//    @Command(name = "Channel Chat History", aliases = {"history", "his"}, help = "Track previous conversation in this channel.")
//    public String getHistory(AbstractEvent event) {
//        MessageChannel channel = event.getMessageEvent().getChannel();
//        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
//        String cs = data.getCleverState();
//        String history = cleverbot.getHistory(cs);
//
//        if (history == null)
//            return "Maybe try talk first and I can grab your history later?";
//        else
//            return String.format("```\n%s\n```", history);
//    }
}
