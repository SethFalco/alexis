/*
 * Alexis - A general purpose chatbot for Discord.
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

package org.elypia.alexis.controllers;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.constraints.Database;
import org.elypia.alexis.services.DatabaseService;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.validation.constraints.Option;

import javax.inject.*;

@Singleton
public class GreetingController implements Controller {

    private final DatabaseService dbService;

    @Inject
    public GreetingController(final DatabaseService dbService) {
        this.dbService = dbService;
    }

    public String setGreeting(
        @Database @Elevated ActionEvent<Event, Message> event,
        @Option({"join", "leave"}) String greeting,
        @Option({"user", "bot"}) String account,
        String message
    ) {
//        MessageReceivedEvent source = (MessageReceivedEvent) event.getSource();
//        Message eventMessage = source.getMessage();
//        Guild guild = eventMessage.getGuild();
//
//        try (Session session = dbService.open()) {
//            GuildData guildData = session.get(GuildData.class, guild.getIdLong());
//            session.createQuery("SELECT f FROM GuildFeature f WHERE f.guild_id = :id", GuildFeature.class);
//            GreetingSettings greetingSettings = data.getSettings().getGreetingSettings();
//        }
//
//        GreetingSetting greetingSetting;
//        if (greeting.equalsIgnoreCase("join"))
//            greetingSetting = greetingSettings.getJoin();
//        else
//            greetingSetting = greetingSettings.getLeave();
//
//        MessageSettings messageSettings;
//        if (account.equalsIgnoreCase("user"))
//            messageSettings = greetingSetting.getUser();
//        else
//            messageSettings = greetingSetting.getBot();
//
//        messageSettings.setMessage(message);
//        messageSettings.setEnabled(true);
//
//        if (messageSettings.getChannel() == 0)
//            messageSettings.setChannel(eventMessage.getChannel().getIdLong());
//
//        data.commit();
//
//        var params = Map.of(
//            "account", account,
//            "greeting", greeting
//        );
//
        return "no u";
    }

    public String setChannel(@Database @Elevated ActionEvent<Event, Message> event, @Talkable TextChannel channel) {
//        MessageReceivedEvent source = (MessageReceivedEvent) event.getSource();
//        GuildData data = GuildData.query(source.getMessage().getGuild().getIdLong());
//        GreetingSettings greetingSettings = data.getSettings().getGreetingSettings();
//        long channelId = channel.getIdLong();
//
//        GreetingSetting welcome = greetingSettings.getJoin();
//        welcome.getUser().setChannel(channelId);
//        welcome.getBot().setChannel(channelId);
//
//        GreetingSetting farewell = greetingSettings.getLeave();
//        farewell.getUser().setChannel(channelId);
//        farewell.getBot().setChannel(channelId);
//
//        data.commit();
//
//        var params = Map.of("mention", channel.getAsMention());
//
        return "no u";
    }
}
