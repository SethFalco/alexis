/*
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.modules;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.*;
import org.elypia.alexis.constraints.Database;
import org.elypia.alexis.entities.GuildData;
import org.elypia.alexis.entities.embedded.*;
import org.elypia.commandler.Commandler;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.metadata.ModuleData;
import org.elypia.commandler.validation.Option;
import org.elypia.jdac.alias.*;
import org.elypia.jdac.validation.*;

import java.util.Map;

@Module(id = "greeting", group = "Settings", aliases = {"greeting", "greetings", "welcome"}, help = "greeting.h")
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
