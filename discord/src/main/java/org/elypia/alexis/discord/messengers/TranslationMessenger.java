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

package org.elypia.alexis.discord.messengers;

import com.google.cloud.translate.Translation;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang.StringEscapeUtils;
import org.elypia.alexis.config.TranslationConfig;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.models.TranslationModel;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class TranslationMessenger implements DiscordMessenger<TranslationModel> {

    /** Logging with slf4j. */
    private static final Logger logger = LoggerFactory.getLogger(TranslationMessenger.class);

    /** Link to Google Translate */
    private static final String GOOGLE_TRANSLATE = "https://translate.google.com/";

    /** Format for the field titles for source and target string. */
    private static final String FIELD_TITLE_FORMAT = "%s (%s)";

    /** Configuration for the translation settings. */
    private final TranslationConfig translationConfig;

    private final AlexisMessages messages;

    @Inject
    public TranslationMessenger(final TranslationConfig translationConfig, AlexisMessages messages) {
        this.translationConfig = translationConfig;
        this.messages = messages;

        if (translationConfig.getAttributionUrl() == null)
            logger.warn("No attribution image set in the configuration. This may be a breach of the attribution guidelines, please see: https://cloud.google.com/translate/attribution");
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, TranslationModel output) {
        Translation translation = output.getTranslation();
        String builder =
            String.format(FIELD_TITLE_FORMAT, messages.translateSource(), translation.getSourceLanguage()) + "\n" +
            output.getSourceText() + "\n\n" +
            String.format(FIELD_TITLE_FORMAT, messages.translateTarget(), output.getTargetLanguage().getCode()) + "\n" +
            translation.getTranslatedText() + "\n\n" +
            GOOGLE_TRANSLATE;

        return new MessageBuilder(builder).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, TranslationModel output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        Translation translation = output.getTranslation();

        String sourceFieldTitle = String.format(FIELD_TITLE_FORMAT, messages.translateSource(), translation.getSourceLanguage());
        builder.addField(sourceFieldTitle, output.getSourceText(), false);

        String targetFieldTitle = String.format(FIELD_TITLE_FORMAT, messages.translateTarget(), output.getTargetLanguage().getCode());
        builder.addField(targetFieldTitle, StringEscapeUtils.unescapeHtml(translation.getTranslatedText()), false);

        String attribution = translationConfig.getAttributionUrl();

        if (attribution != null)
            builder.setImage(attribution);

        builder.setFooter(GOOGLE_TRANSLATE);
        return new MessageBuilder(builder.build()).build();
    }
}
