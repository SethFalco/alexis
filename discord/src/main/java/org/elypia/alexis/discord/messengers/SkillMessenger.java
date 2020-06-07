package org.elypia.alexis.discord.messengers;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.annotation.stereotypes.MessageProvider;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;
import java.util.StringJoiner;

@MessageProvider(provides = Message.class, value = Skill.class)
public class SkillMessenger implements DiscordMessenger<Skill> {

    private final JDA jda;
    private final AlexisMessages messages;

    @Inject
    public SkillMessenger(JDA jda, AlexisMessages messages) {
        this.jda = jda;
        this.messages = messages;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, Skill skill) {
        return null;
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, Skill skill) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(skill.getName());

        StringJoiner joiner = new StringJoiner("\n");

        for (SkillRelation relation : skill.getRelations()) {
            MessageChannelData channelData = relation.getChannelData();
            TextChannel channel = jda.getTextChannelById(channelData.getId());

            if (channel == null)
                throw new IllegalStateException("This shouldn't be possible, can't create a SkillRelation in DMs.");

            joiner.add(channel.getAsMention());
        }

        if (joiner.length() == 0)
            builder.setDescription(messages.skillNotAssigned());
        else
            builder.setDescription(messages.skillAssignedToChannels(joiner.toString()));

        String status = (skill.isEnabled()) ? messages.enabled() : messages.disabled();
        String notifications = (skill.isNotify()) ? messages.enabled() : messages.disabled();

        builder.addField(messages.status(), status, true);
        builder.addField(messages.receiveNotifications(), notifications, true);
        builder.setFooter(messages.uniqueIdentifier(skill.getId()));
        return new MessageBuilder(builder).build();
    }
}
