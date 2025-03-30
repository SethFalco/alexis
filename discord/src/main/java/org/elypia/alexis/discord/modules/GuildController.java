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

package org.elypia.alexis.discord.modules;

import java.time.Duration;

import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.elypia.alexis.core.i18n.AlexisMessages;
import org.elypia.alexis.core.persistence.entities.GuildData;
import org.elypia.alexis.core.persistence.repositories.GuildRepository;
import org.elypia.comcord.constraints.Channels;
import org.elypia.comcord.constraints.Elevated;
import org.elypia.comcord.constraints.Permissions;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.elypia.commandler.newb.AsyncUtils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class GuildController {

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

    @StandardCommand
    public String setDescription(@Channels(ChannelType.TEXT) Message message, @Param @NotBlank String description) {
        long guildId = message.getGuild().getIdLong();
        GuildData guildData = guildRepo.findOptionalBy(guildId).orElse(new GuildData(guildId));

        String oldDescription = guildData.getDescription();

        if (description.equals(oldDescription))
            return messages.guildSameDescriptionAsBefore();

        if (oldDescription == null) {
            guildData.setDescription(description);
            guildRepo.save(guildData);
            return messages.guildSetNewDescription(description);
        }

        guildData.setDescription(description);
        guildRepo.save(guildData);
        return messages.guildChangeDescription(description);
    }

    @StandardCommand
    public String setDataRetentionDuration(@Elevated Message message, @Param Duration duration) {
        long guildId = message.getGuild().getIdLong();
        GuildData guildData = guildRepo.findOptionalBy(guildId).orElse(new GuildData(guildId));
        guildData.setDataRetentionDuration(duration);
        return messages.guildSetDataRetentionDuration();
    }

    @StandardCommand
    public void prune(
        @Channels(ChannelType.TEXT) @Permissions(Permission.MESSAGE_MANAGE) Message message,
        @Param @Min(2) @Max(100) int count,
        @Param(value = "${source.textChannel}", displayAs = "current") TextChannel channel
    ) {
        var contextCopy = AsyncUtils.copyContext();

        channel.getHistoryBefore(message.getIdLong(), count).queue((history) -> {
            channel.deleteMessages(history.getRetrievedHistory()).queue((command) -> {
                var context = AsyncUtils.applyContext(contextCopy);
                message.getChannel().deleteMessageById(message.getIdLong()).queue();
                context.deactivate();
            });
        });
    }
}
