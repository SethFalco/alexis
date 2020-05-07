/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.discord.messengers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.osu.*;

import java.util.StringJoiner;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class OsuPlayerMessenger implements DiscordMessenger<Player> {

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Player output) {
        StringJoiner joiner = new StringJoiner("\n");

        String playerCountry = output.getCountry();

        joiner.add("**__" + output.getUsername() + "__** " + ChatUtils.replaceWithIndicators(playerCountry));
        joiner.add("");
        joiner.add("**Level: **" + (int)output.getLevel());
        joiner.add("**Ranked Score: **" + intf(output.getRankedScore()));
        joiner.add("**Total Score: **" + intf(output.getTotalScore()));
        joiner.add("**Performance Points: **" + decf(output.getPp()));
        joiner.add("**Rank (Country): **" + intf(output.getRank()) + " (" + intf(output.getCountryRank()) + ")");
        joiner.add("**Accuracy: **" + perf(output.getAccuracy()));
        joiner.add("**Play Count: **" + intf(output.getPlayCount()));
        joiner.add("");
        joiner.add(output.getProfileUrl());

        return new MessageBuilder(joiner.toString()).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Player output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        String playerCountry = output.getCountry();

        builder.setThumbnail("https://countryflags.io/" + playerCountry + "/shiny/64.png");

        builder.addField("Username", "[" + output.getUsername() + "](" + output.getProfileUrl() + ") ", true);
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
