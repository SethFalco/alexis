package com.elypia.alexis.providers;

import com.elypia.alexis.utils.DiscordUtils;
import com.elypia.cmdlrdiscord.interfaces.DiscordProvider;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Provider;
import com.elypia.elypiai.steam.SteamGame;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;

import java.util.StringJoiner;

@Provider(provides = Message.class, value = SteamGame.class)
public class GameProvider implements DiscordProvider<SteamGame> {

    @Override
    public Message buildMessage(CommandlerEvent<?> event, SteamGame output) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + output.getName() + "__**");
        joiner.add("");
        joiner.add("**Total Playtime:** " + toPretty(output.getTotalPlaytime()));
        joiner.add("**Recent Playtime:** " + toPretty(output.getRecentPlaytime()));
        joiner.add("");
        joiner.add(output.getUrl());

        return new MessageBuilder(joiner.toString()).build();
    }

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, SteamGame output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setTitle(output.getName(), output.getUrl());
        builder.setThumbnail(output.getIconUrl());
        builder.setImage(output.getLogoUrl());

        String totalPlaytime = toPretty(output.getTotalPlaytime());
        builder.addField("Total Playtime", totalPlaytime, true);

        String recentPlaytime = toPretty(output.getRecentPlaytime());
        builder.addField("Recent Playtime", recentPlaytime, true);

        return new MessageBuilder(builder.build()).build();
    }

    private String toPretty(long playtime) {
        return String.format("%,d Hours", playtime);
    }
}
