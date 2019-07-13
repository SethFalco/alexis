package com.elypia.alexis.google;

import com.elypia.alexis.config.ApiCredentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.*;

import javax.inject.*;
import java.io.*;
import java.util.*;

@Singleton
public class TranslateService {

    private Translate translate;
    private Collection<Language> supported;

    @Inject
    public TranslateService(final ApiCredentials apiCredentials) throws IOException {
        String path = apiCredentials.getGoogle();
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
