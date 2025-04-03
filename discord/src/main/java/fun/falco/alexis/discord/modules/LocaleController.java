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

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.elypia.comcord.annotations.ReactionCommand;
import org.elypia.comcord.constraints.Channels;
import org.elypia.comcord.constraints.Elevated;
import org.elypia.comcord.constraints.Permissions;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;

import com.google.cloud.translate.Language;

import fun.falco.alexis.core.i18n.AlexisMessages;
import fun.falco.alexis.core.modules.translate.TranslateService;
import fun.falco.alexis.core.persistence.entities.GuildData;
import fun.falco.alexis.core.persistence.entities.MessageChannelData;
import fun.falco.alexis.core.persistence.repositories.GuildRepository;
import fun.falco.alexis.core.persistence.repositories.MessageChannelRepository;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

@StandardController
public class LocaleController {

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
     * @param message Message that trigger this event.
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
                        if (previousMessage.getAuthor() == self) {
                            return false;
                        }

                        String content = previousMessage.getContentRaw();
                        return !(content.isBlank() || content.startsWith(self.getAsMention()));
                    })
                    .map(Message::getContentRaw)
                    .collect(Collectors.toList());

                if (previousMessages.isEmpty()) {
                    sender.send("I couldn't find any plain text messages in the past 100 hundred messages that aren't mine.");
                } else {
                    Language language = translateService.detectMostFrequentAsLanguage(previousMessages);
                    Locale locale = Locale.forLanguageTag(language.getCode());
                    sender.send(setGlobalLocale(message, locale));
                }
            }

            context.deactivate();
        });
    }

    @ReactionCommand(emote = "↩️", params = "guild")
    @StandardCommand
    public String setGlobalLocale(@Elevated @Channels(ChannelType.TEXT) Message message, @Param Locale locale) {
        GuildData data = guildRepo.findBy(message.getGuild().getIdLong());
        data.setLocale(locale);
        guildRepo.save(data);

        return messages.localeUpdatedGuild(locale.getDisplayName(locale));
    }

    @StandardCommand
    public String setLocalLocale(@Elevated Message message, @Param Locale locale) {
        MessageChannel channel = message.getChannel();
        long channelId = channel.getIdLong();

        MessageChannelData data = messageChannelRepo.findBy(channelId);

        if (data == null) {
            Long guildId = null;

            if (channel.getType().isGuild()) {
                guildId = ((GuildChannel) channel).getGuild().getIdLong();
            }

            data = new MessageChannelData(channelId, (guildId != null) ? new GuildData(guildId) : null);
        }

        data.setLocale(locale);
        messageChannelRepo.save(data);

        return messages.localeUpdatedChannel(locale.getDisplayName(locale));
    }
}
