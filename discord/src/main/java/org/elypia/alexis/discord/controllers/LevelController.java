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
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.enums.GuildMessageType;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.*;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController
public class LevelController {

    private final GuildRepository guildRepo;
    private final AlexisMessages alexisMessages;

    @Inject
    public LevelController(final GuildRepository guildRepo, final AlexisMessages alexisMessages) {
        this.guildRepo = Objects.requireNonNull(guildRepo);
        this.alexisMessages = Objects.requireNonNull(alexisMessages);
    }

    @StandardCommand
    public String setLevelUpMessage(@Channels(ChannelType.TEXT) @Elevated Message message, @Param @NotBlank String body) {
        long guildId = message.getGuild().getIdLong();
        GuildData guildData = guildRepo.findOptionalBy(guildId).orElse(new GuildData(guildId));
        Map<GuildMessageType, GuildMessage> guildMessages = guildData.getMessages();

        if (guildMessages.containsKey(GuildMessageType.MEMBER_LEVEL_UP)) {
            GuildMessage guildMessage = guildMessages.get(GuildMessageType.MEMBER_LEVEL_UP);
            guildMessage.setMessage(body);
        } else {
            GuildMessage guildMessage = new GuildMessage(guildData, GuildMessageType.MEMBER_LEVEL_UP, null, body);
            guildData.addMessage(guildMessage);
        }

        guildRepo.save(guildData);
        return alexisMessages.levelsSetMessage();
    }

    @StandardCommand
    public String sendMockNotification(@Channels(ChannelType.TEXT) @Elevated Message message) {
        long guildId = message.getGuild().getIdLong();
        GuildData guildData = guildRepo.findBy(guildId);

        if (guildData == null)
            return alexisMessages.notStoringGuildDataYet();

        Map<GuildMessageType, GuildMessage> guildMessages = guildData.getMessages();

        if (!guildMessages.containsKey(GuildMessageType.MEMBER_LEVEL_UP))
            return alexisMessages.levelNoMessage();

        GuildMessage guildMessage = guildMessages.get(GuildMessageType.MEMBER_LEVEL_UP);
        return guildMessage.getMessage();
    }
}
