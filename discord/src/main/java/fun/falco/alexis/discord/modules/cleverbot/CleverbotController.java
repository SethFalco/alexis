/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.discord.modules.cleverbot;

import java.util.Objects;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import fun.falco.alexis.core.persistence.entities.GuildData;
import fun.falco.alexis.core.persistence.entities.MessageChannelData;
import fun.falco.alexis.core.persistence.repositories.MessageChannelRepository;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.elypiai.cleverbot.Cleverbot;

import net.dv8tion.jda.api.entities.Message;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class CleverbotController {

    private final MessageChannelRepository channelRepo;
    private final Cleverbot cleverbot;
    private final MessageSender sender;

    @Inject
    public CleverbotController(MessageChannelRepository channelRepo, CleverbotConfig config, MessageSender sender) {
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
