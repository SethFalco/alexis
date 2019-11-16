/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.messengers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.services.DatabaseService;
import org.elypia.alexis.utils.DiscordUtils;
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

    /** Database connection to query user information stored by the application. */
    private final DatabaseService dbService;

    @Inject
    public UserMessenger(final DatabaseService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, User output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, User output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        Guild guild = EventUtils.getGuild((Event)event.getSource());
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
