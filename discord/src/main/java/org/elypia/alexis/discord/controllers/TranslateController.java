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

import com.google.cloud.translate.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.models.TranslationModel;
import org.elypia.alexis.services.translate.TranslateService;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.*;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.*;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.util.regex.Pattern;

@CommandController
@StandardCommand
public class TranslateController implements Controller {

    /** Matches any mentionable entities. */
    private static final Pattern ENTITY_PATTERN = Pattern.compile("@(?:everyone|here)|<(?:(?:[@#])(?:[&!])?|:\\w{2,}?:)\\d+>");

    private final GuildRepository guildRepo;
    private final TranslateService translateService;
    private final AlexisMessages messages;

    /** {@link RequestScoped} helper object to send messages using {@link Integration#send(ActionEvent, Object)}. */
    private final MessageSender sender;

    @Inject
    public TranslateController(GuildRepository guildRepo, TranslateService translateService, AlexisMessages messages, MessageSender sender) {
        this.guildRepo = guildRepo;
        this.translateService = translateService;
        this.messages = messages;
        this.sender = sender;
    }

    @StandardCommand
    public void translateLastMessage(Message message, @Param Language language) {
        var contextCopy = AsyncUtils.copyContext();

        MessageChannel channel = message.getChannel();
        channel.getHistoryBefore(message.getIdLong(), 1).queue((history) -> {
            var context = AsyncUtils.applyContext(contextCopy);

            if (history.isEmpty())
                sender.send(messages.translateNoLastMessage());
            else {
                Message lastMessage = history.getRetrievedHistory().get(0);

                if (lastMessage.getContentRaw().isBlank() && !lastMessage.getEmbeds().isEmpty())
                    sender.send(messages.translateCantTranslateEmbeds());
                else {
                    String lastMessageContent = lastMessage.getContentRaw();
                    String toTranslate = markNonTranslatableEntities(lastMessageContent);
                    Translation translation = translateService.translate(toTranslate, language);
                    TranslationModel model = new TranslationModel(translation, toTranslate, language);
                    sender.send(model);
                }
            }

            context.deactivate();
        });
    }

    @Default
    @StandardCommand
    public TranslationModel translate(@Everyone Message message, @NotBlank @Param String body, @Param Language language) {
        String toTranslate = markNonTranslatableEntities(body);
        Translation translation = translateService.translate(toTranslate, language);
        return new TranslationModel(translation, toTranslate, language);
    }

    @StandardCommand
    public String toggle(@Channels(ChannelType.TEXT) @Elevated Message message, @Param boolean toggle) {
        guildRepo.updateReactTranslation(toggle, message.getGuild().getIdLong());
        return (toggle) ? messages.featureEnabledTryItNow() : messages.reactionFeatureDisabled();
    }

    // TODO: Implement this
    @StandardCommand
    public String inPrivate(@Channels(ChannelType.TEXT) @Elevated ActionEvent<Event, Message> event, @Param boolean isPrivate) {
        return null;
    }

    private String markNonTranslatableEntities(String content) {
        return content.replaceAll(ENTITY_PATTERN.pattern(), "<span class='notranslate'>$0</span>");
    }
}
