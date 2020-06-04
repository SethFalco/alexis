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
import net.dv8tion.jda.internal.entities.UserImpl;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.persistence.repositories.UserRepository;
import org.elypia.comcord.EventUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;
import java.time.*;
import java.util.StringJoiner;

/**
 * Build a Discord user into an attractive
 * message to show information about the user.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@MessageProvider(provides = Message.class, value = {User.class, UserImpl.class})
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
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add(MarkdownUtil.bold(output.getName()));

        OffsetDateTime now = OffsetDateTime.now();
        long discordAge = getAgeInDays(output.getTimeCreated(), now);

        joiner.add(messages.userJoinedDiscord() + ": " + messages.userJoinAge(output.getTimeCreated(), discordAge));

        Guild guild = EventUtils.getGuild((Event)event.getRequest().getSource());

        if (guild != null) {
            Member member = guild.getMember(output);

            if (member == null)
                throw new UnsupportedOperationException("User info queried for a user in a guild they aren't in. Aborting!");

            joiner.add(messages.userJoinedGuild(guild.getName()) + ": " + messages.userJoinAge(member.getTimeJoined(), discordAge));
        }

        joiner.add(messages.uniqueIdentifier(output.getId()));

        return new MessageBuilder(joiner.toString()).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, User output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        Guild guild = EventUtils.getGuild((Event)event.getRequest().getSource());

        String avatar = output.getEffectiveAvatarUrl();
        builder.setThumbnail(avatar);
        builder.setFooter(messages.uniqueIdentifier(output.getId()), avatar);

        OffsetDateTime now = OffsetDateTime.now();

        MessageEmbed.Field joinedDiscord = createDateField(messages.userJoinedDiscord(), output.getTimeCreated(), now);
        builder.addField(joinedDiscord);

        if (guild == null) {
            builder.setAuthor(output.getName(), avatar);
        } else {
            Member member = guild.getMember(output);

            if (member == null)
                throw new UnsupportedOperationException("User info queried for a user in a guild they aren't in. Aborting!");

            builder.setAuthor(member.getEffectiveName(), avatar);

            MessageEmbed.Field joinedGuild = createDateField(messages.userJoinedGuild(guild.getName()), member.getTimeJoined(), now);
            builder.addField(joinedGuild);
        }

        if (output.isBot())
            builder.addField(messages.userBot(), MarkdownUtil.maskedLink(messages.botInviteLink(), DiscordUtils.getInviteUrl(output)), false);

        return new MessageBuilder(builder).build();
    }

    private MessageEmbed.Field createDateField(String name, OffsetDateTime datetime) {
        return createDateField(name, datetime, OffsetDateTime.now());
    }

    private MessageEmbed.Field createDateField(String name, OffsetDateTime datetime, OffsetDateTime relativeTo) {
        long days = getAgeInDays(datetime, relativeTo);
        String value = messages.userJoinAge(datetime, days);
        return new MessageEmbed.Field(name, value, true);
    }

    private long getAgeInDays(OffsetDateTime datetime, OffsetDateTime relativeTo) {
        return Duration.between(datetime, relativeTo).toDays();
    }
}
