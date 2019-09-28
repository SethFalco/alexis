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
import org.elypia.elypiai.steam.SteamUser;

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
