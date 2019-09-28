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
import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.interfaces.DiscordProvider;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.Provider;
import org.elypia.elypiai.urbandictionary.Definition;

@Provider(provides = Message.class, value = Definition.class)
public class DefinitionProvider implements DiscordProvider<Definition> {

    @Override
    public Message buildMessage(CommandlerEvent<?> event, Definition output) {
        return null;
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, Definition toSend) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setAuthor(toSend.getAuthor());
        String titleText = toSend.getWord();
        builder.setTitle(titleText, toSend.getPermaLink());

        String description = toSend.getDefinition();
        if (description.length() > MessageEmbed.TEXT_MAX_LENGTH)
            description = description.substring(MessageEmbed.TEXT_MAX_LENGTH - 3) + "...";

        builder.setDescription(description);

        String descText = String.format (
            "%s\n\n👍: %,d 👎: %,d",
            toSend.getExample(),
            toSend.getThumbsUp(),
            toSend.getThumbsDown()
        );
        builder.addField("Example", descText, true);

        return new MessageBuilder(builder.build()).build();
    }
}
