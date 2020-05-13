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

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.repositories.UserRepository;
import org.elypia.comcord.EventUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.*;

/**
 * Build a Discord user into an attractive
 * message to show information about the user.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class UserMessenger implements DiscordMessenger<User> {

    private final UserRepository userRepo;
    private final AlexisMessages messages;

    @Inject
    public UserMessenger(UserRepository userRepo, AlexisMessages messages) {
        this.userRepo = userRepo;
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, User output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, User toSend) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        Guild guild = EventUtils.getGuild((Event)event.getRequest().getSource());

        String avatar = toSend.getEffectiveAvatarUrl();
        builder.setThumbnail(avatar);
        builder.setFooter(messages.uniqueIdentifier() + toSend.getId(), avatar);

        OffsetDateTime now = OffsetDateTime.now();

        MessageEmbed.Field joinedDiscord = createDateField(messages.userJoinedDiscord(), toSend.getTimeCreated(), now);
        builder.addField(joinedDiscord);

        if (guild == null) {
            builder.setAuthor(toSend.getName(), avatar);
        } else {
            Member member = guild.getMember(toSend);

            if (member == null)
                throw new UnsupportedOperationException("User info queried for a user in a guild they aren't in. Aborting!");

            builder.setAuthor(member.getEffectiveName(), avatar);

            MessageEmbed.Field joinedGuild = createDateField(messages.userJoinedGuild(guild.getName()), member.getTimeJoined(), now);
            builder.addField(joinedGuild);
        }

        if (toSend.isBot())
            builder.addField(messages.userBot(), MarkdownUtil.maskedLink(messages.botInviteLink(), DiscordUtils.getInviteUrl(toSend)), false);

        return new MessageBuilder(builder).build();
    }

    private MessageEmbed.Field createDateField(String name, OffsetDateTime datetime) {
        return createDateField(name, datetime, OffsetDateTime.now());
    }

    private MessageEmbed.Field createDateField(String name, OffsetDateTime datetime, OffsetDateTime relativeTo) {
        Duration duration = Duration.between(datetime, relativeTo);
        String value = messages.userJoinAge(datetime, duration.toDays());
        return new MessageEmbed.Field(name, value, true);
    }
}
