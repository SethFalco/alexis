/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.discord.messengers;

import javax.inject.Inject;

import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.urbandictionary.Definition;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.discord.utils.DiscordUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@MessageProvider(provides = Message.class, value = Definition.class)
public class DefinitionMessenger implements DiscordMessenger<Definition> {

    private static final String THUMBS_UP = "👍";
    private static final String THUMBS_DOWN = "👎";

    private static final String SCORES_FORMAT = THUMBS_UP + " %,d  " + THUMBS_DOWN + "  %,d";

    private final AlexisMessages messages;

    @Inject
    public DefinitionMessenger(final AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Definition definition) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Definition definition) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setAuthor(definition.getAuthor());
        builder.setTitle(definition.getWord(), definition.getPermaLink());
        builder.setFooter(messages.uniqueIdentifier(definition.getDefinitionId()));

        String description = definition.getDefinitionBody();
        description = ChatUtils.truncateAndAppend(description, MessageEmbed.TEXT_MAX_LENGTH, "...");
        builder.setDescription(description);

        String example = definition.getExample();

        if (example != null && !example.isBlank()) {
            builder.addField(messages.udExampleUsageOfWord(), example, false);
        }

        String descText = String.format(SCORES_FORMAT, definition.getThumbsUp(), definition.getThumbsDown());
        builder.addField(messages.udThumbsUpThumbsDown(), descText, false);

        return new MessageBuilder(builder.build()).build();
    }
}
