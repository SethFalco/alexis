package com.elypia.alexis.commandler.builders;

import com.elypia.alexis.utils.*;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.elypiai.osu.*;
import com.elypia.jdac.alias.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;

import java.util.StringJoiner;

@Compatible(Player.class)
public class OsuPlayerBuilder implements IJDACBuilder<Player> {

    @Override
    public Message buildEmbed(JDACEvent event, Player output) {
        EmbedBuilder builder = BotUtils.newEmbed(event);

        String avatar = output.getAvatarUrl(true);
        builder.setThumbnail(avatar);

        String playerCountry = output.getCountry();
        Country country = Country.get(playerCountry);
        String cPrint = (country != null) ? country.getUnicodeEmote() : playerCountry;

        builder.addField("Username", Md.a(output.getUsername(), output.getProfileUrl()) + " " + cPrint, true);
        builder.addField("Level", String.valueOf((int)output.getLevel()), true);
        builder.addField("Ranked Score", intf(output.getRankedScore()), true);
        builder.addField("Total Score", intf(output.getTotalScore()), true);
        builder.addField("Performance Points", decf(output.getPp()), true);
        builder.addField("Rank (Country)", intf(output.getRank()) + " (" + intf(output.getCountryRank()) + ")", true);
        builder.addField("Accuracy", perf(output.getAccuracy()), true);
        builder.addField("Play Count", intf(output.getPlayCount()), true);

        if (!output.getEvents().isEmpty()) {
            OsuEvent osuEvent = output.getEvents().get(0);
            builder.addField("Latest Activity - " + osuEvent.getDate(), osuEvent.getMessage(), false);
        }

        return new MessageBuilder(builder.build()).build();
    }

    @Override
    public Message build(JDACEvent event, Player output) {
        StringJoiner joiner = new StringJoiner("\n");

        String playerCountry = output.getCountry();
        Country country = Country.get(playerCountry);
        String cPrint = (country != null) ? country.getUnicodeEmote() : playerCountry;

        joiner.add(Md.bu(output.getUsername()) + " " + cPrint);
        joiner.add("");
        joiner.add(Md.b("Level: ") + (int)output.getLevel());
        joiner.add(Md.b("Ranked Score: ") + intf(output.getRankedScore()));
        joiner.add(Md.b("Total Score: ") + intf(output.getTotalScore()));
        joiner.add(Md.b("Performance Points: ") + decf(output.getPp()));
        joiner.add(Md.b("Rank (Country): ") + intf(output.getRank()) + " (" + intf(output.getCountryRank()) + ")");
        joiner.add(Md.b("Accuracy: ") + perf(output.getAccuracy()));
        joiner.add(Md.b("Play Count: ") + intf(output.getPlayCount()));
        joiner.add("");
        joiner.add(output.getProfileUrl());

        return new MessageBuilder(joiner.toString()).build();
    }

    private String intf(long i) {
        return String.format("%,d", i);
    }

    private String decf(double d) {
        return String.format("%,.2f", d);
    }

    private String perf(double d) {
        return String.format("%02.2f%%", d);
    }
}
