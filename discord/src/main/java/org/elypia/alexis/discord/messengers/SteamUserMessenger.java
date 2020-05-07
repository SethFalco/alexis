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
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.discord.utils.DiscordUtils;
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
