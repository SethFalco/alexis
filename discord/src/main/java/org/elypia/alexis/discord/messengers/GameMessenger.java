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
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.elypiai.steam.SteamGame;

import javax.inject.Inject;
import java.util.StringJoiner;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@MessageProvider(provides = Message.class, value = SteamGame.class)
public class GameMessenger implements DiscordMessenger<SteamGame> {

    private final AlexisMessages messages;

    @Inject
    public GameMessenger(final AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, SteamGame output) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + output.getName() + "__**");
        joiner.add("");
        joiner.add("**" + messages.steamTotalPlaytime() + ":** " + toPretty(output.getTotalPlaytime()));
        joiner.add("**" + messages.steamRecentPlaytime() + ":** " + toPretty(output.getRecentPlaytime()));
        joiner.add("");
        joiner.add(output.getUrl());

        return new MessageBuilder(joiner.toString()).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, SteamGame output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setTitle(output.getName(), output.getUrl());
        builder.setThumbnail(output.getIconUrl());
        builder.setImage(output.getLogoUrl());

        String totalPlaytime = toPretty(output.getTotalPlaytime());
        builder.addField(messages.steamTotalPlaytime(), totalPlaytime, true);

        String recentPlaytime = toPretty(output.getRecentPlaytime());
        builder.addField(messages.steamRecentPlaytime(), recentPlaytime, true);

        return new MessageBuilder(builder.build()).build();
    }

    private String toPretty(long playtime) {
        return String.format("%,d %s", playtime, messages.steamPlaytimeHours());
    }
}
