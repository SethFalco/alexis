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

import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.configuration.ApiConfig;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.repositories.MessageChannelRepository;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.elypiai.cleverbot.Cleverbot;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController
public class CleverbotController {

    private static final Logger logger = LoggerFactory.getLogger(CleverbotController.class);

    private final MessageChannelRepository channelRepo;
    private final Cleverbot cleverbot;
    private final MessageSender sender;

    @Inject
    public CleverbotController(MessageChannelRepository channelRepo, ApiConfig config, MessageSender sender) {
        this.channelRepo = Objects.requireNonNull(channelRepo);
        this.sender = sender;
        cleverbot = new Cleverbot(config.getCleverbot());
    }

    @StandardCommand(isDefault = true)
    public void say(Message message, @Param @NotBlank String body) {
        long channelId = message.getChannel().getIdLong();

        GuildData guildData = (message.isFromGuild()) ? new GuildData(message.getGuild().getIdLong()) : null;

        MessageChannelData data = channelRepo.findOptionalBy(channelId)
            .orElse(new MessageChannelData(channelId, guildData));

        String cs = data.getCleverState();

        var contextCopy = AsyncUtils.copyContext();

        cleverbot.say(body, cs).subscribe((response) -> {
            var context = AsyncUtils.applyContext(contextCopy);

            data.setCleverState(response.getCs());
            channelRepo.save(data);
            sender.send(response.getOutput());

            context.deactivate();
        });
    }
}
