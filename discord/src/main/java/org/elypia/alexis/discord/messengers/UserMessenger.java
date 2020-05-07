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
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.repositories.UserRepository;
import org.elypia.comcord.EventUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.*;
import java.time.format.DateTimeFormatter;

/**
 * Build a Discord user into an attractive
 * message to show information about the user.
 *
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class UserMessenger implements DiscordMessenger<User> {

    /** Date/Time Format to display when significant events. */
    private static final DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;

    private final UserRepository userRepo;

    @Inject
    public UserMessenger(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, User output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, User output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        Guild guild = EventUtils.getGuild((Event)event.getRequest().getSource());
        String avatar = output.getEffectiveAvatarUrl();
        builder.setThumbnail(avatar);

        if (guild != null) {
            Member member = guild.getMember(output);
            builder.setAuthor(member.getEffectiveName(), avatar);
            builder.addField("Joined " + guild.getName(), member.getTimeJoined().format(format), true);
        } else {
            builder.setAuthor(output.getName(), avatar);
        }

        builder.addField("Joined Discord", output.getTimeCreated().format(format), true);

        if (output.isBot())
            builder.addField("Bot", "[Invite Link](" + DiscordUtils.getInviteUrl(output) + ")", false);

        builder.setFooter("ID: " + output.getId(), null);

        return new MessageBuilder(builder).build();
    }
}
