package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.events.AbstractEvent;
import com.elypia.commandler.sending.IMessageBuilder;
import com.elypia.elypiai.osu.*;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.StringJoiner;

public class OsuPlayerBuilder implements IMessageBuilder<OsuPlayer> {

    private static final String INT_FORMAT = "%,d";
    private static final String DEC_FORMAT = "%.2f";
    private static final String PERCENT_FORMAT = "%02.2f%%";

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, OsuPlayer toSend) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);

        builder.setThumbnail(toSend.getAvatarUrl());

        builder.addField("Username", Markdown.a(toSend.getUsername(), toSend.getProfileUrl()) + " " + toSend.getCountry().getUnicodeEmote(), true);
        builder.addField("Level", String.valueOf((int)toSend.getLevel()), true);
        builder.addField("Ranked Score", String.format(INT_FORMAT, toSend.getRankedScore()), true);
        builder.addField("Total Score", String.format(INT_FORMAT, toSend.getTotalScore()), true);
        builder.addField("Performance Points", String.format(DEC_FORMAT, toSend.getPp()), true);
        builder.addField("Rank (Country)", String.format(INT_FORMAT, toSend.getRank()) + " (" + String.format(INT_FORMAT, toSend.getCountryRank()) + ")", true);
        builder.addField("Accuracy", String.format(PERCENT_FORMAT, toSend.getAccuracy()), true);
        builder.addField("Play Count", String.format(INT_FORMAT, toSend.getPlayCount()) + "\n_ _", true);

        if (!toSend.getEvents().isEmpty()) {
            OsuEvent osuEvent = toSend.getEvents().get(0);
            builder.addField("Latest Activity - " + osuEvent.getDate(), osuEvent.getMessage(), false);
        }

        return builder.build();
    }

    @Override
    public MessageEmbed buildAsEmbed(AbstractEvent event, OsuPlayer... toSend) {
        return null;
    }

    @Override
    public String buildAsString(AbstractEvent event, OsuPlayer toSend) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + toSend.getUsername() + "__** " + toSend.getCountry().getUnicodeEmote());
        joiner.add("");
        joiner.add("**Level:** " + (int)toSend.getLevel());
        joiner.add("**Ranked Score:** " + String.format(INT_FORMAT, toSend.getRankedScore()));
        joiner.add("**Total Score:** " + String.format(INT_FORMAT, toSend.getTotalScore()));
        joiner.add("**Performance Points:** " + String.format(DEC_FORMAT, toSend.getPp()));
        joiner.add("**Rank (Country):** " + String.format(INT_FORMAT, toSend.getRank()) + " (" + String.format(INT_FORMAT, toSend.getCountryRank()) + ")");
        joiner.add("**Accuracy:** " + String.format(PERCENT_FORMAT, toSend.getAccuracy()));
        joiner.add("**Play Count:** " + String.format("%,d", toSend.getPlayCount()));
        joiner.add("");
        joiner.add(toSend.getProfileUrl());

        return joiner.toString();
    }

    @Override
    public String buildAsString(AbstractEvent event, OsuPlayer... toSend) {
        return null;
    }
}
