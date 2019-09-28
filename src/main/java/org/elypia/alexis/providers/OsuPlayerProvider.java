/*
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.providers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.interfaces.DiscordProvider;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.Provider;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.osu.*;

import java.util.StringJoiner;

@Provider(provides = Message.class, value = Player.class)
public class OsuPlayerProvider implements DiscordProvider<Player> {

    @Override
    public Message buildMessage(CommandlerEvent<?> event, Player output) {
        StringJoiner joiner = new StringJoiner("\n");

        String playerCountry = output.getCountry();

        joiner.add(Md.bu(output.getUsername()) + " " + ChatUtils.replaceWithIndictors(playerCountry));
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

    @Override
    public Message buildEmbed(CommandlerEvent<?> event, Player output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        String avatar = output.getAvatarUrl();
        builder.setThumbnail(avatar);

        String playerCountry = output.getCountry();

        builder.addField("Username", Md.a(output.getUsername(), output.getProfileUrl()) + " " + ChatUtils.replaceWithIndictors(playerCountry), true);
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
