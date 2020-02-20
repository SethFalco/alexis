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

package org.elypia.alexis.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.*;

import javax.inject.*;
import java.io.IOException;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class TranslateService {

    private Translate translate;

    /** Shouldn't be used directly other than in the {@link #getSupportedLangauges()} method. */
    private Collection<Language> supported;

    @Inject
    public TranslateService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        translate = TranslateOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .getService();
    }

    public Translation translate(String text, Locale locale) {
        for (Language language : getSupportedLangauges()) {
            if (language.getCode().equalsIgnoreCase(locale.getLanguage()))
                return translate(text, language);
        }

        throw new IllegalArgumentException("This isn't a supported language.");
    }

    public Translation translate(String text, Language language) {
        return translate(text, language.getCode());
    }

    public Translation translate(String text, String languageCode) {
        return translate.translate(text, Translate.TranslateOption.targetLanguage(languageCode));
    }

    /**
     * @return A list of languages support by the Google Translate API.
     */
    public Collection<Language> getSupportedLangauges() {
        return (supported == null) ? supported = translate.listSupportedLanguages() : supported;
    }
}
