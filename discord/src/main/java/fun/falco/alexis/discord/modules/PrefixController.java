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

package fun.falco.alexis.discord.modules;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import org.elypia.comcord.constraints.Channels;
import org.elypia.comcord.constraints.Elevated;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.core.persistence.entities.GuildData;
import fun.falco.alexis.core.persistence.repositories.GuildRepository;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

@StandardController
public class PrefixController {

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
     * @param guildId ID of the guild to update.
     * @param prefix
     *     New prefix this guild wants to use, or null if no prefix is to be
     *     used.
     */
    private void setPrefix(long guildId, String prefix) {
        GuildData data = guildRepo.findBy(guildId);

        if (data == null) {
            data = new GuildData(guildId);
        }

        data.setPrefix(prefix);
        guildRepo.save(data);
    }
}
