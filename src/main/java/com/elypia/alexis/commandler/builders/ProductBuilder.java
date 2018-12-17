package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.interfaces.IScripts;
import com.elypia.elypiai.amazon.*;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.util.StringJoiner;

@Compatible(Product.class)
public class ProductBuilder implements IJDACBuilder<Product> {

    @Override
    public Message buildEmbed(JDACEvent event, Product output) {
        IScripts scripts = event.getScripts();

        String price = scripts.get("b.amazon.price");
        String footer = scripts.get("b.amazon.footer");

        EmbedBuilder builder = BotUtils.newEmbed(event);

        Attributes attr = output.getAttributes();

        builder.setTitle(attr.getTitle(), output.getUrl());
        builder.setThumbnail(output.getLargeImage().getUrl());
        builder.setFooter(footer, null);

        builder.addField(price, attr.getPrice().getFormattedPrice(), false);

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACEvent event, Product output) {
        IScripts scripts = event.getScripts();

        String price = scripts.get("b.amazon.price");
        String footer = scripts.get("b.amazon.footer");

        StringJoiner joiner = new StringJoiner("\n");

        Attributes attr = output.getAttributes();

        joiner.add("**__" + attr.getTitle() + "__++");
        joiner.add(String.format("**%s:** %s", price, attr.getPrice().getFormattedPrice()));
        joiner.add("");
        joiner.add(output.getUrl());
        joiner.add(footer);

        return new MessageBuilder(joiner.toString()).build();
    }
}
