/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.discord.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

import org.elypia.comcord.constraints.Channels;
import org.elypia.comcord.constraints.Elevated;
import org.elypia.comcord.constraints.Talkable;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.core.persistence.entities.FeatureSettings;
import fun.falco.alexis.core.persistence.entities.GuildData;
import fun.falco.alexis.core.persistence.entities.GuildMessage;
import fun.falco.alexis.core.persistence.enums.Feature;
import fun.falco.alexis.core.persistence.enums.GuildMessageType;
import fun.falco.alexis.core.persistence.repositories.GuildRepository;
import fun.falco.alexis.discord.enums.Greeting;
import fun.falco.alexis.discord.enums.Recipient;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

@StandardController
public class GreetingController {

    private GuildRepository guildRepo;
    private AlexisMessages messages;

    @Inject
    public GreetingController(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.messages = messages;
    }

    @StandardCommand
    public String setEnabled(
        @Channels(ChannelType.TEXT) @Elevated Message message,
        @Param Greeting greeting,
        @Param("true") boolean enabled,
        @Param("both") Recipient recipient
    ) {
        Guild guild = message.getGuild();
        long guildId = guild.getIdLong();
        long userId = message.getAuthor().getIdLong();

        List<Feature> features = getFeatures(greeting, recipient);
        GuildData data = guildRepo.findBy(guildId);
        Map<Feature, FeatureSettings> guildFeatures = data.getFeatures();
        StringJoiner joiner = new StringJoiner("\n");

        for (Feature feature : features) {
            FeatureSettings guildFeature = guildFeatures.get(feature);

            if (guildFeature == null) {
                joiner.add(messages.greetingToggledFeature(feature.getFriendlyName()));
                guildFeatures.put(feature, new FeatureSettings(data, feature, enabled, userId));
                continue;
            }

            if (guildFeature.isEnabled() == enabled) {
                Member lastModifiedBy = guild.getMemberById(guildFeature.getModifiedBy());

                if (lastModifiedBy == null) {
                    joiner.add(messages.greetingModifiedAlreadyByAnonUser(feature.getFriendlyName()));
                } else {
                    joiner.add(messages.greetingModifiedAlready(feature.getFriendlyName(), guildFeature.getModifiedAt(), lastModifiedBy.getEffectiveName()));
                }

                continue;
            }

            guildFeature.setEnabled(enabled);
            guildFeature.setModifiedBy(userId);
            guildFeature.setModifiedAt(new Date());
            joiner.add(messages.greetingUpdatedFeature(feature.getFriendlyName()));
        }

        guildRepo.save(data);
        return joiner.toString();
    }

    @StandardCommand
    public String setGreeting(
        @Channels(ChannelType.TEXT) @Elevated Message eventMessage,
        @Param @NotBlank String body,
        @Param Greeting greeting,
        @Param("both") Recipient recipient
    ) {
        Guild guild = eventMessage.getGuild();
        TextChannel channel = eventMessage.getTextChannel();
        long channelId = channel.getIdLong();

        StringJoiner joiner = new StringJoiner("\n");

        List<GuildMessageType> messageTypes = getMessageTypes(greeting, recipient);
        GuildData data = guildRepo.findBy(guild.getIdLong());
        Map<GuildMessageType, GuildMessage> guildMessages = data.getMessages();

        for (GuildMessageType messageType : messageTypes) {
            GuildMessage oldMessage = guildMessages.get(messageType);

            if (oldMessage == null) {
                joiner.add(messages.greetingSendMessagesTo(messageType.getFriendlyName(), channel.getAsMention()));
                guildMessages.put(messageType, new GuildMessage(data, messageType, channelId, body));
                continue;
            }

            if (!body.equals(oldMessage.getMessage())) {
                String oldBody = oldMessage.getMessage();

                if (oldBody == null) {
                    joiner.add(messages.greetingSetNewMessage(messageType.getFriendlyName(), body));
                } else {
                    joiner.add(messages.greetingUpdateExistingMessage(messageType.getFriendlyName(), body));
                }

                oldMessage.setMessage(body);
            } else {
                joiner.add(messages.greetingMessageNotChanged(messageType.getFriendlyName()));
            }

            if (oldMessage.getChannelId() == null) {
                joiner.add(messages.greetingChannelNotSetSoDefaulting(messageType.getFriendlyName(), channel.getAsMention()));
                oldMessage.setChannelId(channelId);
            } else {
                long oldChannelId = oldMessage.getChannelId();
                TextChannel oldChannel = guild.getTextChannelById(oldChannelId);

                if (oldChannel == null) {
                    joiner.add(messages.greetingChannelSetButDeleted(messageType.getFriendlyName(), channel.getAsMention()));
                    oldMessage.setChannelId(channelId);
                }
            }
        }

        guildRepo.save(data);
        return joiner.toString();
    }

    /**
     * Set where greeting messages should be sent.
     *
     * @param message Message that triggered this event.
     * @param channel Channel to send messages to.
     * @param recipient Types of recipients that should have messages sent there.
     * @return What to reply to this at the end of the command.
     */
    @StandardCommand
    public String setChannel(
        @Channels(ChannelType.TEXT) @Elevated Message message,
        @Param(value = "${source.channel}", displayAs = "current") @Talkable TextChannel channel,
        @Param(value = "both") Recipient recipient
    ) {
        Guild guild = message.getGuild();
        long guildId = guild.getIdLong();

        GuildData guildData = guildRepo.findBy(guildId);

        if (guildData == null) {
            return messages.notStoringGuildDataYet();
        }

        Map<GuildMessageType, GuildMessage> guildMessages = guildData.getMessages();
        long channelId = channel.getIdLong();

        if (recipient == Recipient.USER || recipient == Recipient.BOTH) {
            if (guildMessages.containsKey(GuildMessageType.USER_WELCOME)) {
                guildMessages.get(GuildMessageType.USER_WELCOME).setChannelId(channelId);
            }

            if (guildMessages.containsKey(GuildMessageType.USER_LEAVE)) {
                guildMessages.get(GuildMessageType.USER_LEAVE).setChannelId(channelId);
            }
        }

        if (recipient == Recipient.BOT || recipient == Recipient.BOTH) {
            if (guildMessages.containsKey(GuildMessageType.BOT_WELCOME)) {
                guildMessages.get(GuildMessageType.BOT_WELCOME).setChannelId(channelId);
            }

            if (guildMessages.containsKey(GuildMessageType.BOT_LEAVE)) {
                guildMessages.get(GuildMessageType.BOT_LEAVE).setChannelId(channelId);
            }
        }

        guildRepo.save(guildData);
        return messages.allGreetingMessagesSetToChannel(guild.getName(), channel.getAsMention());
    }

    /**
     * @param greeting Type of greeting we're referring to.
     * @param recipient Type of recipients we're referring to.
     * @return Any {@link GuildMessageType} that is applicable to the specified values.
     */
    private List<GuildMessageType> getMessageTypes(Greeting greeting, Recipient recipient) {
        List<GuildMessageType> messageTypes = new ArrayList<>();

        switch (greeting) {
            case WELCOME:
                if (recipient == Recipient.USER || recipient == Recipient.BOTH) {
                    messageTypes.add(GuildMessageType.USER_WELCOME);
                }

                if (recipient == Recipient.BOT || recipient == Recipient.BOTH) {
                    messageTypes.add(GuildMessageType.BOT_WELCOME);
                }

                break;
            case FAREWELL:
                if (recipient == Recipient.USER || recipient == Recipient.BOTH) {
                    messageTypes.add(GuildMessageType.USER_LEAVE);
                }

                if (recipient == Recipient.BOT || recipient == Recipient.BOTH) {
                    messageTypes.add(GuildMessageType.BOT_LEAVE);
                }

                break;
            default:
                throw new IllegalStateException("Unknown greeting type given to GreetingController.");
        }

        return messageTypes;
    }

    /**
     * @param greeting Type of greeting we're referring to.
     * @param recipient Type of recipients we're referring to.
     * @return Any {@link GuildMessageType} that is applicable to the specified values.
     */
    private List<Feature> getFeatures(Greeting greeting, Recipient recipient) {
        List<Feature> messageTypes = new ArrayList<>();

        switch (greeting) {
            case WELCOME:
                if (recipient == Recipient.USER || recipient == Recipient.BOTH) {
                    messageTypes.add(Feature.USER_JOIN_MESSAGE);
                }

                if (recipient == Recipient.BOT || recipient == Recipient.BOTH) {
                    messageTypes.add(Feature.BOT_JOIN_MESSAGE);
                }

                break;
            case FAREWELL:
                if (recipient == Recipient.USER || recipient == Recipient.BOTH) {
                    messageTypes.add(Feature.USER_LEAVE_MESSAGE);
                }

                if (recipient == Recipient.BOT || recipient == Recipient.BOTH) {
                    messageTypes.add(Feature.BOT_LEAVE_MESSAGE);
                }

                break;
            default:
                throw new IllegalStateException("Unknown feature type given to GreetingController.");
        }

        return messageTypes;
    }
}
