/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.constraints.Database;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.validation.constraints.Option;

import javax.inject.*;

@Singleton
public class GreetingController implements Controller {

    @Inject
    public GreetingController() {
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
