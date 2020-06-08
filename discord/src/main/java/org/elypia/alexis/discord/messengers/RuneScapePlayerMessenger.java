package org.elypia.alexis.discord.messengers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.elypiai.runescape.*;

import javax.inject.Inject;
import java.util.*;

@MessageProvider(provides = Message.class, value = Player.class)
public class RuneScapePlayerMessenger implements DiscordMessenger<Player> {

    private static final String GREEN_CIRCLE = "\uD83D\uDFE2";
    private static final String YELLOW_CIRCLE = "\uD83D\uDFE1";
    private static final String ORANGE_CIRCLE = "\uD83D\uDFE0";
    private static final String RED_CIRCLE = "\uD83D\uDD34";

    private final AlexisMessages messages;

    @Inject
    public RuneScapePlayerMessenger(final AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Player player) {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("**__" + player.getUsername() + "__** ");
        joiner.add("");
        joiner.add("**" + messages.runescapeTotalXp() + ": **" + player.getTotalXpFormatted());
        joiner.add("**" + messages.runescapeCombatLevel() + ": **" + player.getCombatLevel());
        joiner.add("**" + messages.runescapeTotalLevel() + ": **" + player.getTotalLevelFormatted());
        joiner.add("**" + messages.runescapeRank() + ": **" + String.format("%,d", player.getRank()));

        int completed = player.getQuestsComplete();
        int started = player.getQuestsStarted();
        int notStarted = player.getQuestsNotStarted();
        joiner.add("**" + messages.runescapeQuestStatusesTitle() + ": **" + messages.runeScapeQuestStatuses(GREEN_CIRCLE, completed, YELLOW_CIRCLE, started, RED_CIRCLE, notStarted));

        List<Activity> activities = player.getActivities();

        if (!activities.isEmpty()) {
            Activity activity = activities.get(0);
            joiner.add("\n**" + messages.runescapeLatestActivity(activity.getDate()) + "**\n" + activity.getDetails());
        }

        joiner.add("");
        joiner.add(player.getLeaderboardUrl());

        return new MessageBuilder(joiner.toString()).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Player player) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);

        builder.setTitle(player.getUsername(), player.getLeaderboardUrl());
        builder.addField(messages.runescapeTotalXp(), player.getTotalXpFormatted(), true);
        builder.addField(messages.runescapeCombatLevel(), String.valueOf(player.getCombatLevel()), true);
        builder.addField(messages.runescapeTotalLevel(), player.getTotalLevelFormatted(), true);
        builder.addField(messages.runescapeRank(), String.format("%,d", player.getRank()), true);

        int completed = player.getQuestsComplete();
        int started = player.getQuestsStarted();
        int notStarted = player.getQuestsNotStarted();
        builder.addField(messages.runescapeQuestStatusesTitle(), messages.runeScapeQuestStatuses(GREEN_CIRCLE, completed, YELLOW_CIRCLE, started, RED_CIRCLE, notStarted), true);

        List<Activity> activities = player.getActivities();

        if (!activities.isEmpty()) {
            Activity activity = activities.get(0);
            builder.addField(messages.runescapeLatestActivity(activity.getDate()), activity.getDetails(), false);
        }

        return new MessageBuilder(builder).build();
    }
}
