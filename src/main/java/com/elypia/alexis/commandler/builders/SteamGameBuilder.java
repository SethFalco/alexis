package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.elypiai.steam.SteamGame;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.util.StringJoiner;

@Compatible(SteamGame.class)
public class SteamGameBuilder implements IJDACBuilder<SteamGame> {

    @Override
    public Message buildEmbed(JDACEvent event, SteamGame output) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

        builder.setTitle(output.getName(), output.getUrl());
        builder.setThumbnail(output.getIconUrl());
        builder.setImage(output.getLogoUrl());

        String totalPlaytime = toPretty(output.getTotalPlaytime());
        builder.addField("Total Playtime", totalPlaytime, true);

        String recentPlaytime = toPretty(output.getRecentPlaytime());
        builder.addField("Recent Playtime", recentPlaytime, true);

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACEvent event, SteamGame output) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + output.getName() + "__**");
        joiner.add("");
        joiner.add("**Total Playtime:** " + toPretty(output.getTotalPlaytime()));
        joiner.add("**Recent Playtime:** " + toPretty(output.getRecentPlaytime()));
        joiner.add("");
        joiner.add("<http://store.steampowered.com/app/35140>");

        return new MessageBuilder(joiner.toString()).build();
    }

    private String toPretty(long playtime) {
        return String.format("%,d Hours", playtime);
    }
}
