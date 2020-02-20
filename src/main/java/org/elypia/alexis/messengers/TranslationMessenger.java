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

package org.elypia.alexis.messengers;

import com.google.cloud.translate.Translation;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Message;
import org.elypia.alexis.config.TranslationConfig;
import org.elypia.alexis.models.TranslationModel;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.api.DiscordMessenger;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class TranslationMessenger implements DiscordMessenger<TranslationModel> {

    private static final String GOOGLE_TRANSLATE = "https://translate.google.com/";

    private final TranslationConfig translationConfig;

    @Inject
    public TranslationMessenger(final TranslationConfig translationConfig) {
        this.translationConfig = translationConfig;
    }

    @Override
    public Message buildMessage(ActionEvent<?, Message> event, TranslationModel output) {
        Translation translation = output.getTranslation();
        String builder =
            "Source (" + translation.getSourceLanguage() + ")\n" +
            output.getSourceText() + "\n\n" +
            "Target (" + output.getTargetLanguage().getCode() + ")\n" +
            translation.getTranslatedText() + "\n\n" +
            GOOGLE_TRANSLATE;
        return new MessageBuilder(builder).build();
    }

    @Override
    public Message buildEmbed(ActionEvent<?, Message> event, TranslationModel output) {
        EmbedBuilder builder = DiscordUtils.newEmbed(event);
        Translation translation = output.getTranslation();
        builder.addField("Source (" + translation.getSourceLanguage() + ")", output.getSourceText(), false);
        builder.addField("Target (" + output.getTargetLanguage().getCode() + ")", translation.getTranslatedText(), false);

        String attribution = translationConfig.getAttributionUrl();

        if (attribution != null)
            builder.setImage(attribution);

        builder.setFooter(GOOGLE_TRANSLATE);
        return new MessageBuilder(builder.build()).build();
    }
}
