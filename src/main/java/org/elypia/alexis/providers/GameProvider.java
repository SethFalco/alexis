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

package org.elypia.alexis.providers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.interfaces.DiscordProvider;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.Provider;
import org.elypia.elypiai.steam.SteamGame;

import java.util.StringJoiner;

@Provider(provides = Message.class, value = SteamGame.class)
public class GameProvider implements DiscordProvider<SteamGame> {

    @Override
    public Message buildMessage(CommandlerEvent<?> event, SteamGame output) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + output.getName() + "__**");
        joiner.add("");
        joiner.add("**Total Playtime:** " + toPretty(output.getTotalPlaytime()));
        joiner.add("**Recent Playtime:** " + toPretty(output.getRecentPlaytime()));
        joiner.add("");
        joiner.add(output.getUrl());

        return new MessageBuilder(joiner.toString()).build();
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, SteamGame output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setTitle(output.getName(), output.getUrl());
        builder.setThumbnail(output.getIconUrl());
        builder.setImage(output.getLogoUrl());

        String totalPlaytime = toPretty(output.getTotalPlaytime());
        builder.addField("Total Playtime", totalPlaytime, true);

        String recentPlaytime = toPretty(output.getRecentPlaytime());
        builder.addField("Recent Playtime", recentPlaytime, true);

        return new MessageBuilder(builder.build()).build();
    }

    private String toPretty(long playtime) {
        return String.format("%,d Hours", playtime);
    }
}
