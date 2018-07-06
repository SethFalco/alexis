package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.sending.IMessageBuilder;
import com.elypia.elypiai.urbandictionary.UrbanDefinition;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class UrbanDefinitionBuilder implements IMessageBuilder<UrbanDefinition> {

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, UrbanDefinition toSend) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);

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

        return builder.build();
    }

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, UrbanDefinition... toSend) {
        return null;
    }

    @Override
    public String buildAsString(AbstractEvent event, UrbanDefinition toSend) {
        return null;
    }

    @Override
    public String buildAsString(AbstractEvent event, UrbanDefinition... toSend) {
        return null;
    }
}
