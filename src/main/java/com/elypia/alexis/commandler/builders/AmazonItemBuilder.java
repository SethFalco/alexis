//package com.elypia.alexis.commandler.builders;
//
//import com.elypia.alexis.utils.BotUtils;
//import com.elypia.commandler.events.AbstractEvent;
//import com.elypia.commandler.sending.IMessageBuilder;
//import com.elypia.elypiai.amazon.AmazonItem;
//import net.dv8tion.jda.core.EmbedBuilder;
//import net.dv8tion.jda.core.entities.MessageEmbed;
//
//import java.util.StringJoiner;
//
//public class AmazonItemBuilder implements IMessageBuilder<AmazonItem> {
//
//    private static final String FOOTER = "We get income from purchases under our Amazon links! ^-^";
//
//    @Override
//    public MessageEmbed buildAsEmbed(AbstractEvent event, AmazonItem toSend) {
//        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);
//
//        builder.setTitle(toSend.getTitle(), toSend.getUrl());
//        builder.setThumbnail(toSend.getImage());
//        builder.setFooter(FOOTER, null);
//
//        builder.addField("Price", toSend.getPriceString(), false);
//
//        return builder.build();
//    }
//
//    @Override
//    public MessageEmbed buildAsEmbed(AbstractEvent event, AmazonItem... toSend) {
//        return null;
//    }
//
//    @Override
//    public String buildAsString(AbstractEvent event, AmazonItem toSend) {
//        StringJoiner joiner = new StringJoiner("\n");
//
//        joiner.add("**__" + toSend.getTitle() + "__++");
//        joiner.add("**Price:** " + toSend.getPriceString());
//        joiner.add("");
//        joiner.add(toSend.getUrl());
//        joiner.add(FOOTER);
//
//        return joiner.toString();
//    }
//
//    @Override
//    public String buildAsString(AbstractEvent event, AmazonItem... toSend) {
//        return null;
//    }
//}
