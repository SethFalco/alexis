package com.elypia.alexis.handlers.modules.settings;

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

import java.util.*;

@Elevated
@Database
@Module(name = "greeting", group = "Settings", aliases = {"greeting", "greetings", "welcome"}, help = "greeting.h")
public class GreetingModule extends JDAHandler {

    @Command(name = "greeting.message", aliases = "message", help = "greeting.message.h")
    @Param(name = "greeting.message.p.greeting", help = "greeting.message.p.greeting.h")
    @Param(name = "greeting.message.p.account.", help = "greeting.message.p.account.h")
    @Param(name = "common.message", help = "greeting.message.p.message.h")
    public String setGreeting(JDACommand event, @Option({"join", "leave"}) String greeting, @Option({"user", "bot"}) String account, String message) {
        Message eventMessage = event.getMessage();
        Guild guild = eventMessage.getGuild();
        GuildData data = GuildData.query(guild.getIdLong());
        GreetingSettings greetingSettings = data.getSettings().getGreetingSettings();

        GreetingSetting greetingSetting;
        if (greeting.equalsIgnoreCase("join"))
            greetingSetting = greetingSettings.getJoin();
        else
            greetingSetting = greetingSettings.getLeave();

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

        var params = Map.of(
            "account", account,
            "greeting", greeting
        );

        return BotUtils.getScript("greeting.message.response", event.getSource(), params);
    }

    @Command(name = "greeting.channel", aliases = "channel", help = "greeting.channel.h")
    @Param(name = "common.channel", help = "greeting.channel.p.channel.h")
    public String setChannel(JDACommand event, @Talkable TextChannel channel) {
        GuildData data = GuildData.query(event.getMessage().getGuild().getIdLong());
        GreetingSettings greetingSettings = data.getSettings().getGreetingSettings();
        long channelId = channel.getIdLong();

        GreetingSetting welcome = greetingSettings.getJoin();
        welcome.getUser().setChannel(channelId);
        welcome.getBot().setChannel(channelId);

        GreetingSetting farewell = greetingSettings.getLeave();
        farewell.getUser().setChannel(channelId);
        farewell.getBot().setChannel(channelId);

        data.commit();

        var params = Map.of("mention", channel.getAsMention());

        return BotUtils.getScript("greeting.channel.response", event.getSource(), params);
    }
}
