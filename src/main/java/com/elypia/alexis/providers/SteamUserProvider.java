/*
 * Copyright (C) 2019-2019  Elypia
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

package com.elypia.alexis.providers;

import com.elypia.alexis.utils.DiscordUtils;
import com.elypia.cmdlrdiscord.interfaces.DiscordProvider;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.elypia.elypiai.steam.SteamUser;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;

@Provider(provides = Message.class, value = SteamUser.class)
public class SteamUserProvider implements DiscordProvider<SteamUser> {

    @Override
    public Message buildMessage(CommandlerEvent<?> event, SteamUser output) {
        return null;
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, SteamUser toSend) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setTitle(toSend.getUsername(), toSend.getProfileUrl());
        builder.setThumbnail(toSend.getAvatarHigh());

        return new MessageBuilder(builder.build()).build();
    }
}
