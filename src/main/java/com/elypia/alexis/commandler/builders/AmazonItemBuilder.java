package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;
import com.elypia.elypiai.amazon.AmazonItem;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.util.StringJoiner;

public class AmazonItemBuilder implements IJDABuilder<AmazonItem> {

    private static final String FOOTER = "We get income from purchases under our Amazon links! ^-^";

    @Override
    public Message buildEmbed(JDACommand event, AmazonItem output) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);

        builder.setTitle(output.getTitle(), output.getUrl());
        builder.setThumbnail(output.getImage());
        builder.setFooter(FOOTER, null);

        builder.addField("Price", output.getFormattedPrice(), false);

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACommand event, AmazonItem output) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + output.getTitle() + "__++");
        joiner.add("**Price:** " + output.getFormattedPrice());
        joiner.add("");
        joiner.add(output.getUrl());
        joiner.add(FOOTER);

        return new MessageBuilder(joiner.toString()).build();
    }
}
