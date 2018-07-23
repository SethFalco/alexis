package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.commandler.validators.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.*;
import com.elypia.commandler.*;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.validation.command.Elevated;
import com.elypia.commandler.annotations.validation.param.*;
import net.dv8tion.jda.core.entities.*;

@Elevated
@Database
@Module(name = "Welcome and Farewell", aliases = {"greeting", "greetings", "welcome"}, help = "Configure where and what is sent when a user or bot joins and leaves the guild.")
public class GreetingModule extends JDAHandler {

    @Command(name = "Set Greeting Message", aliases = "message", help = "Set a greeting messages for the given event and account type.")
    @Param(name = "greeting", help = "The greeting type, either `welcome` or `farewell`.")
    @Param(name = "account", help = "The account type, either `user` or `bot`.")
    @Param(name = "message", help = "The message to send when this event occurs.")
    public String setGreeting(JDACommand event, @Option({"welcome", "farewell"}) String greeting, @Option({"user", "bot"}) String account, String message) {
        Message eventMessage = event.getMessage();
        Guild guild = eventMessage.getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        GreetingSettings greetingSettings = data.getSettings().getGreetingSettings();

        GreetingSetting greetingSetting;
        if (greeting.equalsIgnoreCase("welcome"))
            greetingSetting = greetingSettings.getWelcome();
        else
            greetingSetting = greetingSettings.getFarewell();

        MessageSettings messageSettings;
        if (account.equalsIgnoreCase("user"))
            messageSettings = greetingSetting.getUser();
        else
            messageSettings = greetingSetting.getBot();

        messageSettings.setMessage(message);
        messageSettings.setEnabled(true);

        if (messageSettings.getChannel() == 0)
            messageSettings.setChannel(eventMessage.getChannel().getIdLong());

        data.commit();
        return "I've enabled " + account + " " + greeting + " messages on this channel!";
    }

    @Command(name = "Set Greeting Channel", aliases = "channel", help = "Change the channel that you display bot messages without affecting other settings.")
    @Param(name = "channel", help = "The channel you want greeting messages to be sent too.")
    public String setChannel(JDACommand event, @Talkable TextChannel channel) {
        GuildData data = GuildData.query(event.getMessage().getGuild().getIdLong());
        GreetingSettings greetingSettings = data.getSettings().getGreetingSettings();
        long channelId = channel.getIdLong();

        GreetingSetting welcome = greetingSettings.getWelcome();
        welcome.getUser().setChannel(channelId);
        welcome.getBot().setChannel(channelId);

        GreetingSetting farewell = greetingSettings.getFarewell();
        farewell.getUser().setChannel(channelId);
        farewell.getBot().setChannel(channelId);

        data.commit();
        return "I've set the channel for all messages to go through to " + channel.getAsMention() + ".";
    }
}
