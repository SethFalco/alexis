/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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

    /**
     * The URL that attributes Google as the translation provider.
     *
     * @see <a href="https://cloud.google.com/translate/attribution">Cloud Translation Attribution Requirements</a>
     */
    private final String ATTRIBUTION;

    @Inject
    public TranslationMessenger(final TranslationConfig config) {
        this.ATTRIBUTION = config.getAttributionUrl();
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

        if (ATTRIBUTION != null)
            builder.setImage(ATTRIBUTION);

        builder.setFooter(GOOGLE_TRANSLATE);
        return new MessageBuilder(builder.build()).build();
    }
}
