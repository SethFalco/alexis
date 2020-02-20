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

package org.elypia.alexis.messengers;

import com.github.twitch4j.helix.domain.User;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class TwitchUserMessenger implements DiscordMessenger<User> {

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, User output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, User output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setAuthor(output.getDisplayName(), "https://twitch.tv/" + output.getLogin());
        builder.setThumbnail(output.getProfileImageUrl());
        builder.setDescription(output.getDescription());
        builder.addField("Type", output.getBroadcasterType(), true);
        builder.addField("Total Views", String.format("%,d", output.getViewCount()), true);

        return new MessageBuilder(builder.build()).build();
    }
}
