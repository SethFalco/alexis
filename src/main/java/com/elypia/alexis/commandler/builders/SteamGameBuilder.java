package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.sending.IMessageBuilder;
import com.elypia.elypiai.steam.SteamGame;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.StringJoiner;

public class SteamGameBuilder implements IMessageBuilder<SteamGame> {

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, SteamGame toSend) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);

        builder.setTitle(toSend.getName(), toSend.getUrl());
        builder.setThumbnail(toSend.getIconUrl());
        builder.setImage(toSend.getLogoUrl());

        String totalPlaytime = toPretty(toSend.getTotalPlaytime());
        builder.addField("Total Playtime", totalPlaytime, true);

        String recentPlaytime = toPretty(toSend.getRecentPlaytime());
        builder.addField("Recent Playtime", recentPlaytime, true);

        return builder.build();
    }

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, SteamGame... toSend) {
        return null;
    }

    @Override
    public String buildAsString(AbstractEvent event, SteamGame toSend) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + toSend.getName() + "__**");
        joiner.add("");
        joiner.add("**Total Playtime:** " + toPretty(toSend.getTotalPlaytime()));
        joiner.add("**Recent Playtime:** " + toPretty(toSend.getRecentPlaytime()));
        joiner.add("");
        joiner.add("<http://store.steampowered.com/app/35140>");

        return joiner.toString();
    }

    @Override
    public String buildAsString(AbstractEvent event, SteamGame... toSend) {
        return null;
    }

    private String toPretty(long playtime) {
        return String.format("%,d Hours", playtime);
    }
}
