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
