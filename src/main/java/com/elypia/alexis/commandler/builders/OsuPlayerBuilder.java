package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.*;
import com.elypia.elypiai.osu.*;
import com.elypia.elypiai.utils.Markdown;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Message;

import java.util.StringJoiner;

public class OsuPlayerBuilder implements IJDABuilder<OsuPlayer> {

    private static final String INT_FORMAT = "%,d";
    private static final String DEC_FORMAT = "%.2f";
    private static final String PERCENT_FORMAT = "%02.2f%%";

    @Override
    public Message buildEmbed(JDACommand event, OsuPlayer output) {
        EmbedBuilder builder = BotUtils.createEmbedBuilder(event);

        builder.setThumbnail(output.getAvatarUrl());

        builder.addField("Username", Markdown.a(output.getUsername(), output.getProfileUrl()) + " " + output.getCountry().getUnicodeEmote(), true);
        builder.addField("Level", String.valueOf((int)output.getLevel()), true);
        builder.addField("Ranked Score", String.format(INT_FORMAT, output.getRankedScore()), true);
        builder.addField("Total Score", String.format(INT_FORMAT, output.getTotalScore()), true);
        builder.addField("Performance Points", String.format(DEC_FORMAT, output.getPp()), true);
        builder.addField("Rank (Country)", String.format(INT_FORMAT, output.getRank()) + " (" + String.format(INT_FORMAT, output.getCountryRank()) + ")", true);
        builder.addField("Accuracy", String.format(PERCENT_FORMAT, output.getAccuracy()), true);
        builder.addField("Play Count", String.format(INT_FORMAT, output.getPlayCount()) + "\n_ _", true);

        if (!output.getEvents().isEmpty()) {
            OsuEvent osuEvent = output.getEvents().get(0);
            builder.addField("Latest Activity - " + osuEvent.getDate(), osuEvent.getMessage(), false);
        }

        return new MessageBuilder(builder.toString()).build();
    }

    @Override
    public Message build(JDACommand event, OsuPlayer output) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + output.getUsername() + "__** " + output.getCountry().getUnicodeEmote());
        joiner.add("");
        joiner.add("**Level:** " + (int)output.getLevel());
        joiner.add("**Ranked Score:** " + String.format(INT_FORMAT, output.getRankedScore()));
        joiner.add("**Total Score:** " + String.format(INT_FORMAT, output.getTotalScore()));
        joiner.add("**Performance Points:** " + String.format(DEC_FORMAT, output.getPp()));
        joiner.add("**Rank (Country):** " + String.format(INT_FORMAT, output.getRank()) + " (" + String.format(INT_FORMAT, output.getCountryRank()) + ")");
        joiner.add("**Accuracy:** " + String.format(PERCENT_FORMAT, output.getAccuracy()));
        joiner.add("**Play Count:** " + String.format("%,d", output.getPlayCount()));
        joiner.add("");
        joiner.add(output.getProfileUrl());

        return new MessageBuilder(joiner.toString()).build();
    }
}
