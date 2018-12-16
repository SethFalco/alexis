package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.jda.*;
import com.elypia.elypiai.amazon.AmazonItem;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.util.StringJoiner;

public class AmazonItemBuilder implements IJDABuilder<AmazonItem> {

    @Override
    public Message buildEmbed(JDACommand event, AmazonItem output) {
        String price = event.getScript("b.amazon.price");
        String footer = event.getScript("b.amazon.footer");

        EmbedBuilder builder = BotUtils.newEmbed(event);

        builder.setTitle(output.getTitle(), output.getUrl());
        builder.setThumbnail(output.getImage());
        builder.setFooter(footer, null);

        builder.addField(price, output.getFormattedPrice(), false);

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACommand event, AmazonItem output) {
        String price = event.getScript("b.amazon.price");
        String footer = event.getScript("b.amazon.footer");

        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + output.getTitle() + "__++");
        joiner.add(String.format("**%s:** %s", price, output.getFormattedPrice()));
        joiner.add("");
        joiner.add(output.getUrl());
        joiner.add(footer);

        return new MessageBuilder(joiner.toString()).build();
    }
}
