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
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.elypiai.steam.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class SteamUserMessenger implements DiscordMessenger<SteamUser> {

    /** Base URL for SteamDB, append the users ID to get info for a specific user. */
    private static final String STEAM_DB = "https://steamdb.info/calculator/";

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, SteamUser output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, SteamUser output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setTitle(output.getUsername(), output.getProfileUrl());
        builder.setDescription("You can get more information from [SteamDB](" + STEAM_DB + output.getId() + ").");
        builder.setThumbnail(output.getAvatarHigh());
        builder.addField("Last Log Off", output.getLastLogOff().toString(), false);
        builder.addField("Time Created", output.getTimeCreated().toString(), false);

        GameSession session = output.getCurrentlyPlaying();

        if (session != null)
            builder.addField("Currently Playing", "[" + session.getGameStatus() + "](" + session.getAppUrl() + ")", true);

        builder.setFooter("Steam ID: " + output.getId());
        return new MessageBuilder(builder.build()).build();
    }
}
