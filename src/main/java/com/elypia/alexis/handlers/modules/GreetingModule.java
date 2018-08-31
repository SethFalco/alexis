package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.commandler.validators.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.*;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.jda.annotations.validation.command.Elevated;
import com.elypia.commandler.annotations.validation.param.*;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.jda.annotations.validation.param.Talkable;
import net.dv8tion.jda.core.entities.*;

import java.util.HashMap;

@Elevated
@Database
@Module(name = "greeting.title", aliases = {"greeting", "greetings", "welcome"}, help = "greeting.help")
public class GreetingModule extends JDAHandler {

    @Command(name = "greeting.message.title", aliases = "message", help = "greeting.message.help")
    @Param(name = "greeting", help = "greeting.message.greeting.help")
    @Param(name = "account", help = "greeting.message.account.help")
    @Param(name = "message", help = "greeting.message.message.help")
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

        HashMap<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("greeting", greeting);

        return BotUtils.getScript("greeting.message.response", event.getSource(), params);
    }

    @Command(name = "greeting.channel.title", aliases = "channel", help = "greeting.channel.help")
    @Param(name = "channel", help = "greeting.channel.channel.help")
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

        HashMap<String, Object> params = new HashMap<>();
        params.put("mention", channel.getAsMention());

        return BotUtils.getScript("greeting.channel.response", event.getSource(), params);
    }
}
