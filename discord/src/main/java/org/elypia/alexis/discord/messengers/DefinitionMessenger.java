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
import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.elypiai.urbandictionary.Definition;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class DefinitionMessenger implements DiscordMessenger<Definition> {

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Definition output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Definition toSend) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setAuthor(toSend.getAuthor());
        String titleText = toSend.getWord();
        builder.setTitle(titleText, toSend.getPermaLink());

        String description = toSend.getDefinition();
        if (description.length() > MessageEmbed.TEXT_MAX_LENGTH)
            description = description.substring(MessageEmbed.TEXT_MAX_LENGTH - 3) + "...";

        builder.setDescription(description);

        // TODO: Unexpected behavior when there is no example
        String descText = String.format (
            "%s%n%n: %,d 👎: %,d",
            toSend.getExample(),
            toSend.getThumbsUp(),
            toSend.getThumbsDown()
        );
        builder.addField("Example", descText, true);

        return new MessageBuilder(builder.build()).build();
    }
}
