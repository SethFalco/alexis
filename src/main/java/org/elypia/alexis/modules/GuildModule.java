/*
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

package org.elypia.alexis.modules;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.annotations.Scoped;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.*;

import javax.inject.Inject;
import javax.validation.constraints.*;
import java.util.Collection;

@Module(name = "guild", group = "Discord", aliases = "guild")
public class GuildModule implements Handler {

    private final LanguageInterface lang;

    @Inject
    public GuildModule(LanguageInterface lang) {
        this.lang = lang;
    }

    @Command(name = "info", aliases = "info")
    public EmbedBuilder info(
        CommandlerEvent<Event> event,
        @Param(name = "guild", defaultValue = "${src.guild.id}") @Scoped Guild guild
    ) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setThumbnail(guild.getIconUrl());
        builder.setTitle(guild.getName());
        builder.addField(lang.get(event, "guild.owner"), guild.getOwner().getEffectiveName(), true);

        Collection<Member> members = guild.getMembers();
        long bots = members.stream().map(Member::getUser).filter(User::isBot).count();
        String memberField = String.format("%,d (%,d)", members.size() - bots, bots);
        builder.addField(lang.get(event, "guild.users"), memberField, true);

        return builder;
    }

    @Command(name = "prune", aliases = "prune")
    public void prune(
        @Channels(ChannelType.TEXT) @Permissions(Permission.MESSAGE_MANAGE) CommandlerEvent<?> event,
        @Param(name = "count") @Min(2) @Max(100) int count,
        @Param(name = "channel", defaultValue = "${src.channel.id}") TextChannel channel
    ) {
        MessageReceivedEvent source = (MessageReceivedEvent)event.getSource();

        channel.getHistoryBefore(source.getMessageIdLong(), count).queue(history -> {
            channel.deleteMessages(history.getRetrievedHistory()).queue(command ->
                source.getMessage().delete().queue()
            );
        });
    }
}
