package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;

public class UrbanDefinitionBuilder implements IJDABuilder<UrbanDefinition> {

    @Override
    public Message buildEmbed(JDACommand event, UrbanDefinition toSend) {
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

    @Override
    public Message build(JDACommand event, UrbanDefinition output) {
        return null;
    }
}
