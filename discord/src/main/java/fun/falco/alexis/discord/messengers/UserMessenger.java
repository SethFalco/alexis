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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.StringJoiner;

import javax.inject.Inject;

import org.elypia.comcord.EventUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.discord.utils.DiscordUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import net.dv8tion.jda.internal.entities.UserImpl;

/**
 * Build a Discord user into an attractive
 * message to show information about the user.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@MessageProvider(provides = Message.class, value = {User.class, UserImpl.class})
public class UserMessenger implements DiscordMessenger<User> {

    private final AlexisMessages messages;

    @Inject
    public UserMessenger(AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, User output) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add(MarkdownUtil.bold(output.getName()));

        OffsetDateTime now = OffsetDateTime.now();
        long discordAge = getAgeInDays(output.getTimeCreated(), now);

        joiner.add(messages.userJoinedDiscord() + ": " + messages.userJoinAge(output.getTimeCreated(), discordAge));

        Guild guild = EventUtils.getGuild((Event) event.getRequest().getSource());

        if (guild != null) {
            Member member = guild.getMember(output);

            if (member == null) {
                throw new UnsupportedOperationException("User info queried for a user in a guild they aren't in. Aborting!");
            }

            joiner.add(messages.userJoinedGuild(guild.getName()) + ": " + messages.userJoinAge(member.getTimeJoined(), discordAge));
        }

        joiner.add(messages.uniqueIdentifier(output.getId()));

        return new MessageBuilder(joiner.toString()).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, User output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        Guild guild = EventUtils.getGuild((Event) event.getRequest().getSource());

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

            if (member == null) {
                throw new UnsupportedOperationException("User info queried for a user in a guild they aren't in. Aborting!");
            }

            builder.setAuthor(member.getEffectiveName(), avatar);

            MessageEmbed.Field joinedGuild = createDateField(messages.userJoinedGuild(guild.getName()), member.getTimeJoined(), now);
            builder.addField(joinedGuild);
        }

        if (output.isBot()) {
            builder.addField(messages.userBot(), MarkdownUtil.maskedLink(messages.botInviteLink(), DiscordUtils.getInviteUrl(output)), false);
        }

        return new MessageBuilder(builder).build();
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
