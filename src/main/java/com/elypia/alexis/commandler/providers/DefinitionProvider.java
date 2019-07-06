package com.elypia.alexis.commandler.providers;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.elypia.commandler.discord.interfaces.DiscordProvider;
import com.elypia.elypiai.urbandictionary.Definition;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

@Provider(provides = Message.class, value = Definition.class)
public class DefinitionProvider implements DiscordProvider<Definition> {

    @Override
    public Message buildMessage(CommandlerEvent<?> event, Definition output) {
        return null;
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, Definition toSend) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

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
