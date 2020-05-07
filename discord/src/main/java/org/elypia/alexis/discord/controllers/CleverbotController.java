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
import org.elypia.alexis.config.ApiConfig;
import org.elypia.alexis.constraints.Database;
import org.elypia.alexis.entities.MessageChannelData;
import org.elypia.alexis.repositories.MessageChannelRepository;
import org.elypia.comcord.EventUtils;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.elypiai.cleverbot.*;
import org.slf4j.*;

import javax.inject.*;
import java.io.IOException;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class CleverbotController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(CleverbotController.class);

    private final MessageChannelRepository channelRepo;
    private final Cleverbot cleverbot;

    @Inject
    public CleverbotController(final MessageChannelRepository channelRepo, final ApiConfig config) {
        this.channelRepo = Objects.requireNonNull(channelRepo);
        cleverbot = new Cleverbot(config.getCleverbot());
    }

    public Object say(@Database ActionEvent<Event, Message> event, String body) throws IOException {
        MessageChannel channel = EventUtils.getMessageChannel(event.getRequest().getSource());

        MessageChannelData data = channelRepo.findBy(channel.getIdLong());
        String cs = data.getCleverState();

        Optional<CleverResponse> optResponse = cleverbot.say(body, cs).complete();

        if (optResponse.isEmpty())
            return "The request failed for some reason.";

        CleverResponse response = optResponse.get();
        channelRepo.updateCleverState(response.getCs(), channel.getIdLong());
        return response.getOutput();
    }
}
