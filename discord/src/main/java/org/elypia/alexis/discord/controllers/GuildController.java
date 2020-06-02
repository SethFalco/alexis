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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.persistence.entities.GuildData;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;

import javax.inject.Inject;
import javax.validation.constraints.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@CommandController
@StandardCommand
public class GuildController implements Controller {

    private final GuildRepository guildRepo;
    private final AlexisMessages messages;

    @Inject
    public GuildController(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.messages = messages;
    }

    @StandardCommand
    public Guild info(@Param(value = "${source.guild}", displayAs = "current") Guild guild) {
        return guild;
    }

    // TODO: Make a way to unset things?
    @StandardCommand
    public String setDescription(@Channels(ChannelType.TEXT) Guild guild, @Param @NotBlank String description) {
        GuildData data = guildRepo.findBy(guild.getIdLong());
        String oldDescription = data.getDescription();

        if (description.equals(oldDescription))
            return messages.guildSameDescriptionAsBefore();

        if (oldDescription == null) {
            data.setDescription(description);
            return messages.guildSetNewDescription(description);
        }

        data.setDescription(description);
        return messages.guildChangeDescription(description);
    }

    @StandardCommand
    public void prune(@Channels(ChannelType.TEXT) @Permissions(Permission.MESSAGE_MANAGE) Message message, @Param @Min(2) @Max(100) int count, @Param(value = "${source.textChannel}", displayAs = "current") TextChannel channel) {
        channel.getHistoryBefore(message.getIdLong(), count).queue((history) -> {
            channel.deleteMessages(history.getRetrievedHistory()).queue((command) ->
                message.getChannel().deleteMessageById(message.getIdLong()).queue()
            );
        });
    }
}
