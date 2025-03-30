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

package fun.falco.alexis.discord.messengers;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.discord.utils.DiscordUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;

import com.github.twitch4j.helix.domain.User;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@MessageProvider(provides = Message.class, value = User.class)
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

        if (!broadcasterType.isEmpty()) {
            builder.addField(messages.twitchType(), StringUtils.capitalize(broadcasterType), true);
        }

        builder.setFooter(messages.uniqueIdentifier(output.getId()), avatarUrl);
        return new MessageBuilder(builder.build()).build();
    }
}
