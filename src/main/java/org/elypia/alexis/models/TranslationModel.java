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

package org.elypia.alexis.models;

import com.google.cloud.translate.*;

import java.util.Objects;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class TranslationModel {

    /** The Google Translation object. */
    private final Translation translation;

    /** The source string used as input when translating. */
    private final String source;

    /** The target language to translate to, used as input when translating. */
    private final Language targetLanguage;

    public TranslationModel(final Translation translation, final String source, final Language targetLanguage) {
        this.translation = Objects.requireNonNull(translation);
        this.source = Objects.requireNonNull(source);
        this.targetLanguage = Objects.requireNonNull(targetLanguage);
    }

    public Translation getTranslation() {
        return translation;
    }

    public String getSourceText() {
        return source;
    }

    public Language getTargetLanguage() {
        return targetLanguage;
    }
}
