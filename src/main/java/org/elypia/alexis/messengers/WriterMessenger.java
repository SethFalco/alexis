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
import org.elypia.elypiai.nanowrimo.Writer;

import javax.inject.Singleton;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class WriterMessenger implements DiscordMessenger<Writer> {

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Writer output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Writer output) {
        String name = output.getUsername();

        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setAuthor(name, output.getUrl());

        String fieldString = String.format("%,d", output.getWordCount());
        builder.addField("Word Count", fieldString, true);

        if (output.isWinner())
            builder.setFooter("Is a winner", null);

        return new MessageBuilder(builder.build()).build();
    }
}
