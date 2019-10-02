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

package org.elypia.alexis.modules;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.elypia.alexis.ChatBot;
import org.elypia.alexis.constraints.Database;
import org.elypia.alexis.entities.MessageChannelData;
import org.elypia.commandler.annotations.*;
import org.elypia.elypiai.cleverbot.Cleverbot;
import org.elypia.jdac.alias.*;
import org.slf4j.*;

@Module(id = "Cleverbot", aliases = {"cleverbot", "cb"}, help = "cb.help")
public class CleverbotModule extends JDACHandler {

    private static final Logger logger = LoggerFactory.getLogger(CleverbotModule.class);

    private Cleverbot cleverbot;

    public CleverbotModule() {
        cleverbot = new Cleverbot(ChatBot.configurationService.getApiCredentials().getCleverbot());
    }

    @Command(id = "cb.say", aliases = {"say", "ask"}, help = "cb.say.help")
    @Param(id = "common.body", help = "cb.param.body.help")
    public void say(@Database JDACEvent event, String body) {
        MessageChannel channel = event.getSource().getChannel();
        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
        String cs = data.getCleverState();

        cleverbot.say(body, cs).queue(response -> {
            event.send(response.getOutput());

            data.setCleverState(response.getCs());
            data.commit();
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }

//    @Command(name = "Channel Chat History", aliases = {"history", "his"}, help = "cb.history.help")
//    public String getHistory(JDACommand event) {
//        MessageChannel channel = event.getMessageEvent().getChannel();
//        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
//        String cs = data.getCleverState();
//        String history = cleverbot.getHistory(cs);
//
//        if (history == null)
//            return "DiscordUtils.getScript("cb.no_history", event.getSource())";
//        else
//            return String.format("```\n%s\n```", history);
//    }
}
