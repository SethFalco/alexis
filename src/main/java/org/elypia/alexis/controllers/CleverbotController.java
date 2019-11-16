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
import org.elypia.alexis.config.ApiConfig;
import org.elypia.alexis.constraints.Database;
import org.elypia.alexis.entities.MessageChannelData;
import org.elypia.alexis.services.DatabaseService;
import org.elypia.comcord.EventUtils;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.elypiai.cleverbot.*;
import org.hibernate.Session;
import org.slf4j.*;

import javax.inject.*;
import java.io.IOException;
import java.util.Optional;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class CleverbotController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(CleverbotController.class);

    private final DatabaseService dbService;
    private final Cleverbot cleverbot;

    @Inject
    public CleverbotController(final DatabaseService dbService, final ApiConfig config) {
        this.dbService = dbService;
        cleverbot = new Cleverbot(config.getCleverbot());
    }

    public Object say(@Database ActionEvent<Event, Message> event, String body) throws IOException {
        MessageChannel channel = EventUtils.getMessageChannel(event.getSource());

        try (Session session = dbService.open()) {
            MessageChannelData data = session.get(MessageChannelData.class, channel.getIdLong());
            String cs = data.getCleverState();

            Optional<CleverResponse> optResponse = cleverbot.say(body, cs).complete();

            if (optResponse.isEmpty())
                return "The request failed for some reason.";

            CleverResponse response = optResponse.get();
            data.setCleverState(response.getCs());
            session.save(data);
            return response.getOutput();
        }
    }
}
