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

package org.elypia.alexis.discord.messengers;

import com.github.twitch4j.helix.domain.User;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang.StringUtils;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class TwitchUserMessenger implements DiscordMessenger<User> {

    private final AlexisMessages messages;

    @Inject
    public TwitchUserMessenger(final AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, User output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, User output) {
        String avatarUrl = output.getProfileImageUrl();
        String broadcasterType = output.getBroadcasterType();

        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setAuthor(output.getDisplayName(), "https://twitch.tv/" + output.getLogin());
        builder.setThumbnail(avatarUrl);
        builder.setDescription(output.getDescription());
        builder.addField(messages.twitchTotalViews(), String.format("%,d", output.getViewCount()), true);

        if (!broadcasterType.isEmpty())
            builder.addField(messages.twitchType(), StringUtils.capitalize(broadcasterType), true);

        builder.setFooter(messages.uniqueIdentifier() + output.getId(), avatarUrl);
        return new MessageBuilder(builder.build()).build();
    }
}
