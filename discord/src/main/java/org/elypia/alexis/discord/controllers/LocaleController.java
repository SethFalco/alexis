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

package org.elypia.alexis.discord.controllers;

import com.google.cloud.translate.Language;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.repositories.*;
import org.elypia.alexis.services.translate.TranslateService;
import org.elypia.comcord.annotations.ReactionCommand;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.dispatchers.standard.*;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@StandardController
public class LocaleController implements Controller {

    private final GuildRepository guildRepo;
    private final MessageChannelRepository messageChannelRepo;
    private final AlexisMessages messages;
    private final TranslateService translateService;
    private final MessageSender sender;

    @Inject
    public LocaleController(GuildRepository guildRepo, MessageChannelRepository messageChannelRepo, AlexisMessages messages, TranslateService translateService, MessageSender sender) {
        this.guildRepo = guildRepo;
        this.messageChannelRepo = messageChannelRepo;
        this.messages = messages;
        this.translateService = translateService;
        this.sender = sender;
    }

    /**
     * This scans the last 100 messages sent in a channel and sets
     * the locale of the guild based on what language is detected
     * most often. This will not check all messages found, it will
     * first filter through them and remove messaged that just contain
     * embeds or are sent by the bot itself.
     *
     * @param message The message that trigger this event.
     */
    @StandardCommand
    public void detectLocaleLanguage(@Permissions(Permission.MESSAGE_HISTORY) @Elevated @Channels(ChannelType.TEXT) Message message) {
        long messageId = message.getIdLong();
        User self = message.getJDA().getSelfUser();
        MessageChannel channel = message.getChannel();

        var contextCopy = AsyncUtils.copyContext();

        channel.getHistoryBefore(messageId, 100).queue((history) -> {
            var context = AsyncUtils.applyContext(contextCopy);

            if (history.isEmpty()) {
                sender.send("I won't be able to detect the language here because there is no message history.");
            } else {
                List<String> previousMessages = history.getRetrievedHistory()
                    .stream()
                    .filter((previousMessage) -> {
                        if (previousMessage.getAuthor() == self)
                            return false;

                        String content = previousMessage.getContentRaw();
                        return !(content.isBlank() || content.startsWith(self.getAsMention()));
                    })
                    .map(Message::getContentRaw)
                    .collect(Collectors.toList());

                if (previousMessages.isEmpty())
                    sender.send("I couldn't find any plain text messages in the past 100 hundred messages that aren't mine.");
                else {
                    Language language = translateService.detectMostFrequentAsLanguage(previousMessages);
                    sender.send("I've set the language to " + language.getName() + " " + language.getCode());
                }
            }

            context.deactivate();
        });
    }

    @ReactionCommand(emote = "\u21a9\ufe0f", params = "guild")
    @StandardCommand
    public String setGlobalLocale(@Elevated @Channels(ChannelType.TEXT) Message message, @Param Locale locale) {
        GuildData data = guildRepo.findBy(message.getGuild().getIdLong());
        data.setLocale(locale);
        guildRepo.save(data);

        return messages.localeUpdatedGuild(locale.getDisplayName(locale));
    }

    // TODO: make a way for users to use aliases in a single language
    // TODO: default help controller has unexpected result on $help modules osu!
    @StandardCommand
    public String setLocalLocale(@Elevated Message message, @Param Locale locale) {
        MessageChannel channel = message.getChannel();
        long channelId = channel.getIdLong();

        MessageChannelData data = messageChannelRepo.findBy(channelId);

        if (data == null) {
            Long guildId = null;

            if (channel.getType().isGuild())
                guildId = ((GuildChannel)channel).getGuild().getIdLong();

            data = new MessageChannelData(channelId, (guildId != null) ? new GuildData(guildId) : null);
        }

        data.setLocale(locale);
        messageChannelRepo.save(data);

        return messages.localeUpdatedChannel(locale.getDisplayName(locale));
    }
}
