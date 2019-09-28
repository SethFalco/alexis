/*
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

package org.elypia.alexis.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.*;
import org.elypia.alexis.configuration.ApiConfig;

import javax.inject.*;
import java.io.*;
import java.util.*;

@Singleton
public class TranslateService {

    private Translate translate;
    private Collection<Language> supported;

    @Inject
    public TranslateService(final ApiConfig apiConfig) throws IOException {
        String path = apiConfig.getGoogle();
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(path));

        translate = TranslateOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .getService();

        supported = translate.listSupportedLanguages();
    }

    public Translation translate(String text, Locale locale) {
        for (Language language : supported) {
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
        return supported;
    }
}
