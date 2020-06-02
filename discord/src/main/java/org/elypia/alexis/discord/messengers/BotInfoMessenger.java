package org.elypia.alexis.discord.messengers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.discord.models.BotInfoModel;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;

@MessageProvider(provides = Message.class, value = BotInfoModel.class)
public class BotInfoMessenger implements DiscordMessenger<BotInfoModel> {

    private final AlexisMessages messages;

    @Inject
    public BotInfoMessenger(AlexisMessages messages) {
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, BotInfoModel output) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, BotInfoModel model) {
        final String botInviteUrl = model.getBotInviteUrl();
        final String botAvatarUrl = model.getBotAvatarUrl();
        final String supportGuildText = model.getSupportGuildText();

        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        builder.setAuthor(model.getAuthorName(), model.getAuthorUrl(), model.getAuthorLogo());
        builder.setTitle(model.getBotName(), botInviteUrl);
        builder.setThumbnail(botAvatarUrl);
        builder.setFooter(messages.uniqueIdentifier(String.valueOf(model.getBotId())), botAvatarUrl);

        builder.setDescription(messages.botDescription(model.getBotDescription(), botInviteUrl));
        builder.addField(messages.botTotalGuilds(), String.format("%,d", model.getTotalGuilds()), true);

        String userField = String.format("%,d (%,d)", model.getTotalUsers(), model.getTotalBots());
        builder.addField(messages.totalBotsAndUsers(), userField, true);

        if (supportGuildText != null)
            builder.addField(messages.botSupportGuild(), model.getSupportGuildText(), false);

        return new MessageBuilder(builder).build();
    }
}
