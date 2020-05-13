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
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.urbandictionary.Definition;

import javax.inject.Inject;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class DefinitionMessenger implements DiscordMessenger<Definition> {

    private static final String THUMBS_UP = "\uD83D\uDC4D";
    private static final String THUMBS_DOWN = "\uD83D\uDC4E";

    private static final String SCORES_FORMAT = THUMBS_UP + " %,d  " + THUMBS_DOWN + "  %,d";

    private final AlexisMessages messages;

    @Inject
    public DefinitionMessenger(final AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Definition output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Definition toSend) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setAuthor(toSend.getAuthor());
        builder.setTitle(toSend.getWord(), toSend.getPermaLink());

        String description = toSend.getDefinition();
        description = ChatUtils.truncateAndAppend(description, MessageEmbed.TEXT_MAX_LENGTH, "...");
        builder.setDescription(description);

        String example = toSend.getExample();

        if (example != null && !example.isBlank())
            builder.addField(messages.udExampleUsageOfWord(), example, false);

        String descText = String.format(SCORES_FORMAT, toSend.getThumbsUp(), toSend.getThumbsDown());
        builder.addField(messages.udThumbsUpThumbsDown(), descText, false);

        return new MessageBuilder(builder.build()).build();
    }
}
