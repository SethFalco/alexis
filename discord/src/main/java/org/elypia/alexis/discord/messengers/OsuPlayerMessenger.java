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
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.osu.*;

import javax.inject.Inject;
import java.util.StringJoiner;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@MessageProvider(provides = Message.class, value = Player.class)
public class OsuPlayerMessenger implements DiscordMessenger<Player> {

    private final AlexisMessages messages;

    @Inject
    public OsuPlayerMessenger(final AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Player player) {
        StringJoiner joiner = new StringJoiner("\n");
        String playerCountry = player.getCountry();

        joiner.add("**__" + player.getUsername() + "__** " + ChatUtils.replaceWithIndicators(playerCountry));
        joiner.add("");
        joiner.add("**" + messages.userLevel() + ": **" + (int)player.getLevel());
        joiner.add("**" + messages.osuRankedScore() + ": **" + intf(player.getRankedScore()));
        joiner.add("**" + messages.osuTotalScore() + ": **" + intf(player.getTotalScore()));
        joiner.add("**" + messages.osuPp() + ": **" + decf(player.getPp()));
        joiner.add("**" + messages.osuRank() + ": **" + intf(player.getRank()) + " (" + intf(player.getCountryRank()) + ")");
        joiner.add("**" + messages.osuAccuracy() + ": **" + perf(player.getAccuracy()));
        joiner.add("**" + messages.osuPlayCount() + ": **" + intf(player.getPlayCount()));
        joiner.add(messages.uniqueIdentifier(player.getId()));
        joiner.add("");
        joiner.add(player.getProfileUrl());

        return new MessageBuilder(joiner.toString()).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Player player) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        String playerCountry = player.getCountry();

        builder.setThumbnail("https://countryflags.io/" + playerCountry + "/shiny/64.png");
        builder.setFooter(messages.uniqueIdentifier(player.getId()), null);

        builder.addField(messages.osuUsername(), MarkdownUtil.maskedLink(player.getUsername(), player.getProfileUrl()), true);
        builder.addField(messages.userLevel(), String.valueOf((int)player.getLevel()), true);
        builder.addField(messages.osuRankedScore(), intf(player.getRankedScore()), true);
        builder.addField(messages.osuTotalScore(), intf(player.getTotalScore()), true);
        builder.addField(messages.osuPp(), decf(player.getPp()), true);
        builder.addField(messages.osuRank(), intf(player.getRank()) + " (" + intf(player.getCountryRank()) + ")", true);
        builder.addField(messages.osuAccuracy(), perf(player.getAccuracy()), true);
        builder.addField(messages.osuPlayCount(), intf(player.getPlayCount()), true);

        if (!player.getEvents().isEmpty()) {
            OsuEvent osuEvent = player.getEvents().get(0);
            builder.addField(messages.osuLatestActivity() + " - " + osuEvent.getDate(), osuEvent.getMessage(), false);
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
