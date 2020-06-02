package org.elypia.alexis.discord.messengers;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.discord.models.*;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 3.0.0
 */
@MessageProvider(provides = Message.class, value = EmoteLeaderboardModel.class)
public class EmoteLeaderboardMessenger implements DiscordMessenger<EmoteLeaderboardModel> {

    private final AlexisMessages messages;

    @Inject
    public EmoteLeaderboardMessenger(AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, EmoteLeaderboardModel leaderboard) {
        List<EmoteLeaderboardEntryModel> entries = leaderboard.getEntries();

        int entriesLength = String.valueOf(entries.size()).length();
        String localUsageTitle = messages.emoteLeaderboardLocal();
        String globalUsageTitle = messages.emoteLeaderboardGlobal();
        String format = "`| %" + entriesLength + "s |`  %s  `| %" + localUsageTitle.length() + "s |`  `| %" + globalUsageTitle.length() + "s |`";

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("**" + messages.emoteLeaderboardTitle() + "**");
        joiner.add(String.format(format, "#", "\u2b07\ufe0f", localUsageTitle, globalUsageTitle));

        for (int i = 0; i < entries.size(); i++) {
            EmoteLeaderboardEntryModel entry = entries.get(i);
            String emoteMention = entry.getEmote().getAsMention();
            String localUsage = String.format("%,d", entry.getLocaleUsage());
            String globalUsage = String.format("%,d", entry.getGlobalUsage());
            joiner.add(String.format(format, i + 1, emoteMention, localUsage, globalUsage));
        }

        return new MessageBuilder(joiner.toString()).build();
    }
}
