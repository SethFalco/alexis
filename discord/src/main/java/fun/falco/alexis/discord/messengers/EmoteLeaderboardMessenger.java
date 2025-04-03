package fun.falco.alexis.discord.messengers;

import java.util.List;
import java.util.StringJoiner;

import javax.inject.Inject;

import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.discord.models.EmoteLeaderboardEntryModel;
import fun.falco.alexis.discord.models.EmoteLeaderboardModel;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

/**
 * @author seth@falco.fun (Seth Falco)
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
        joiner.add(String.format(format, "#", "⬇️", localUsageTitle, globalUsageTitle));

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
