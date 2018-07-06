//package com.elypia.alexis.commandler.builders;
//
//import com.elypia.alexis.utils.BotUtils;
//import com.elypia.commandler.events.AbstractEvent;
//import com.elypia.commandler.sending.IMessageBuilder;
//import com.elypia.elypiai.myanimelist.Anime;
//import net.dv8tion.jda.core.EmbedBuilder;
//import net.dv8tion.jda.core.entities.MessageEmbed;
//import org.apache.commons.lang3.StringUtils;
//
//public class AnimeBuilder implements IMessageBuilder<Anime> {
//
//    @Override
//    public MessageEmbed buildAsEmbed(AbstractEvent event, Anime toSend) {
//        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);
//
//        String rating = StringUtils.repeat("\\⭐", (int)toSend.getScore());
//        builder.setTitle(toSend.getTitle() + " | Rating: " + rating);
//        builder.setThumbnail(toSend.getImageUrl());
//
//        builder.addField("Type", toSend.getType().getApiName(), true);
//        builder.addField("Status", toSend.getStatus(), true);
//
//        int episodeCount = toSend.getEpisodes();
//        String episodes = episodeCount == 0 ? "Ongoing" : String.valueOf(episodeCount);
//        builder.addField("Episodes", episodes, true);
//
//        String endDate = toSend.getEndDate();
//        String airing = toSend.getStartDate() + " - " + (endDate != null ? endDate : "Ongoing");
//        builder.addField("Airing", airing, true);
//
//        String syn = toSend.getSynopsis();
//
//        if (syn.length() >= MessageEmbed.VALUE_MAX_LENGTH)
//            syn = syn.substring(0, MessageEmbed.VALUE_MAX_LENGTH - 3) + "...";
//
//        builder.addField("Synposis", syn, false);
//
//        return builder.build();
//    }
//
//    @Override
//    public MessageEmbed buildAsEmbed(AbstractEvent event, Anime... toSend) {
//        return null;
//    }
//
//    @Override
//    public String buildAsString(AbstractEvent event, Anime toSend) {
//        return null;
//    }
//
//    @Override
//    public String buildAsString(AbstractEvent event, Anime... toSend) {
//        return null;
//    }
//}
