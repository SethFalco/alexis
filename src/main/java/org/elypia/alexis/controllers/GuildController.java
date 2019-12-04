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

package org.elypia.alexis.controllers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Singleton;
import javax.validation.constraints.*;
import java.util.Collection;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class GuildController implements Controller {

    public MessageEmbed info(Guild guild) {
        EmbedBuilder builder = DiscordUtils.newEmbed(guild);
        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());

        Member owner = guild.getOwner();
        String ownerValue = (owner != null) ? owner.getUser().getAsTag() : "Deleted/Banned";
        builder.addField("Guild Owner", ownerValue, true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField("Total Users (Bots)", memberField, true);

        return builder.build();
    }

    public void prune(
        @Channels(ChannelType.TEXT) @Permissions(Permission.MESSAGE_MANAGE) ActionEvent<Event, Message> event,
        @Min(2) @Max(100) int count,
        TextChannel channel
    ) {
        GenericMessageEvent source = (GenericMessageEvent)event.getRequest().getSource();

        channel.getHistoryBefore(source.getMessageIdLong(), count).queue(history -> {
            channel.deleteMessages(history.getRetrievedHistory()).queue(command ->
                source.getChannel().deleteMessageById(source.getMessageId()).queue()
            );
        });
    }
}
