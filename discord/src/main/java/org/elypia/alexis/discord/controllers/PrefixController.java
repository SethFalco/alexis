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
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.persistence.entities.GuildData;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

@StandardController
public class PrefixController {

    private static final Logger logger = LoggerFactory.getLogger(PrefixController.class);

    private final GuildRepository guildRepo;
    private final AlexisMessages messages;

    @Inject
    public PrefixController(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.messages = messages;
    }

    @StandardCommand(isDefault = true)
    public String changePrefix(@Elevated @Channels(ChannelType.TEXT) Message message, @Param @NotBlank String prefix) {
        long guildId = message.getGuild().getIdLong();
        setPrefix(guildId, prefix);
        return messages.prefixHasBeenChanged(prefix);
    }

    @StandardCommand
    public String enableMentionOnly(@Elevated @Channels(ChannelType.TEXT) Message message) {
        long guildId = message.getGuild().getIdLong();
        setPrefix(guildId, null);
        return messages.disablePrefixMentionsOnly();
    }

    /**
     * Actually set the prefix in the database.
     *
     * @param guildId The ID of the guild to update.
     * @param prefix The new prefix this guild wants to use,
     *               or null if no prefix is to be used.
     */
    private void setPrefix(long guildId, String prefix) {
        GuildData data = guildRepo.findBy(guildId);

        if (data == null)
            data = new GuildData(guildId);

        data.setPrefix(prefix);
        guildRepo.save(data);
    }
}
