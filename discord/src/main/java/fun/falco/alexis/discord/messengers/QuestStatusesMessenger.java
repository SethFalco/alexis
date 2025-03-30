package fun.falco.alexis.discord.messengers;

import java.util.stream.Collectors;

import javax.inject.Inject;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.core.modules.runescape.QuestStatusModel;
import fun.falco.alexis.discord.utils.DiscordUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.runescape.QuestStatus;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

@MessageProvider(provides = Message.class, value = QuestStatusModel.class)
public class QuestStatusesMessenger implements DiscordMessenger<QuestStatusModel> {

    private final AlexisMessages messages;

    @Inject
    public QuestStatusesMessenger(final AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, QuestStatusModel questsModel) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, QuestStatusModel questsModel) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setTitle(questsModel.getUsername());

        questsModel.getQuestStatuses().forEach((status, quests) -> {
            String statusDisplayName;

            switch (status) {
                case COMPLETED:
                    statusDisplayName = messages.questsComplete();
                    break;
                case STARTED:
                    statusDisplayName = messages.questsStarted();
                    break;
                case NOT_STARTED:
                    statusDisplayName = messages.questsNotStarted();
                    break;
                default:
                    throw new IllegalStateException("Unknown quest status was provided.");
            }

            String value = quests.stream()
                .map(QuestStatus::getTitle)
                .collect(Collectors.joining(", "));

            value = ChatUtils.truncateAndAppend(value, MessageEmbed.VALUE_MAX_LENGTH, "...");

            builder.addField(statusDisplayName, value, false);
        });

        return new MessageBuilder(builder).build();
    }
}
