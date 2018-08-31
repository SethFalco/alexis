package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.commandler.validators.Database;
import com.elypia.alexis.entities.MessageChannelData;
import com.elypia.alexis.utils.DiscordLogger;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.cleverbot.Cleverbot;
import net.dv8tion.jda.core.entities.MessageChannel;

// ! Make a way for users to add tweaks to Cleverbot
@Database
@Module(name = "CleverBot", aliases = {"cleverbot", "cb"}, help = "help.cleverbot")
public class CleverbotHandler extends JDAHandler {

    private Cleverbot cleverbot;

    public CleverbotHandler() {
        cleverbot = new Cleverbot(Alexis.config.getApiKeys().getCleverbot());
    }

    @Command(name = "Talk to Cleverbot", aliases = {"say", "ask"}, help = "help.cleverbot.say")
    @Param(name = "body", help = "help.cleverbot.say.body")
    public void say(JDACommand event, String body) {
        MessageChannel channel = event.getSource().getChannel();
        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
        String cs = data.getCleverState();

        cleverbot.say(body, cs).queue(response -> {
            event.reply(response.getOutput());

            data.setCleverState(response.getCs());
            data.commit();
        }, failure -> DiscordLogger.log(event, failure));
    }

// ! This is disabled for now until we have caching available
//    @Command(name = "Channel Chat History", aliases = {"history", "his"}, help = "help.cleverbot.history")
//    public String getHistory(JDACommand event) {
//        MessageChannel channel = event.getMessageEvent().getChannel();
//        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
//        String cs = data.getCleverState();
//        String history = cleverbot.getHistory(cs);
//
//        if (history == null)
//            return "BotUtils.getScript("cleverbot.history.no_history", event.getSource())";
//        else
//            return String.format("```\n%s\n```", history);
//    }
}
