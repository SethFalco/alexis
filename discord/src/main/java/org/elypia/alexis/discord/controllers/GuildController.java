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

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.elypia.alexis.discord.utils.DiscordUtils;
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
