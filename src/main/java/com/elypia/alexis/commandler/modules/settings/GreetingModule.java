package com.elypia.alexis.commandler.modules.settings;

import com.elypia.alexis.commandler.validation.Database;
import com.elypia.alexis.entities.GuildData;
import com.elypia.alexis.entities.embedded.*;
import com.elypia.commandler.Commandler;
import com.elypia.commandler.annotations.Module;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.metadata.ModuleData;
import com.elypia.commandler.validation.Option;
import com.elypia.jdac.alias.*;
import com.elypia.jdac.validation.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;

import java.util.Map;

@Module(id = "Greetings", group = "Settings", aliases = {"greeting", "greetings", "welcome"}, help = "greeting.h")
public class GreetingModule extends JDACHandler {

    /**
     * Initialise the module, this will assign the values
     * in the module and create a {@link ModuleData} which is
     * what {@link Commandler} uses in runtime to identify modules,
     * commands or obtain any static data.
     *
     * @param commandler Our parent Commandler class.
     */
    public GreetingModule(Commandler<GenericMessageEvent, Message> commandler) {
        super(commandler);
    }

    @Command(id = "greeting.message", aliases = "message", help = "greeting.message.h")
    @Param(id = "greeting.message.p.greeting", help = "greeting.message.p.greeting.h")
    @Param(id = "greeting.message.p.account.", help = "greeting.message.p.account.h")
    @Param(id = "common.message", help = "greeting.message.p.message.h")
    public String setGreeting(
        @Database @Elevated JDACEvent event,
        @Option({"join", "leave"}) String greeting,
        @Option({"user", "bot"}) String account,
        String message
    ) {
        MessageReceivedEvent source = (MessageReceivedEvent) event.getSource();
        Message eventMessage = source.getMessage();
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

        return scripts.get(event.getSource(), "greeting.message.response", params);
    }

    @Command(id = "greeting.channel", aliases = "channel", help = "greeting.channel.h")
    @Param(id = "common.channel", help = "greeting.channel.p.channel.h")
    public String setChannel(@Database @Elevated JDACEvent event, @Talkable TextChannel channel) {
        MessageReceivedEvent source = (MessageReceivedEvent) event.getSource();
        GuildData data = GuildData.query(source.getMessage().getGuild().getIdLong());
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

        return scripts.get(event.getSource(), "greeting.channel.response", params);
    }
}
