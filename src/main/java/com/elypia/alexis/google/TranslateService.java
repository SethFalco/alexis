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
    private Map<com.elypia.alexis.utils.Language, Language> languages;

    @Inject
    public TranslateService(final ApiCredentials apiCredentials) throws IOException {
        String path = apiCredentials.getGoogle();
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(path));

        translate = TranslateOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();

        languages = new HashMap<>();

        List<Language> supportedLanguages = translate.listSupportedLanguages();
        supportedLanguages.forEach(lang -> {
            for (com.elypia.alexis.utils.Language eLang : com.elypia.alexis.utils.Language.values()) {
                if (eLang.getCode().equalsIgnoreCase(lang.getCode())) {
                    languages.put(eLang, lang);
                    return;
                }
            }
        });
    }

    public Translation translate(String text, Language language) {
        return translate.translate(text, Translate.TranslateOption.targetLanguage(language.getCode()));
    }

    public Map.Entry<com.elypia.alexis.utils.Language, Language> getLanguage(String code) {
        for (Map.Entry<com.elypia.alexis.utils.Language, Language> entry : languages.entrySet()) {
            if (entry.getKey().getCode().equalsIgnoreCase(code))
                return entry;
        }

        return null;
    }

    public Map<com.elypia.alexis.utils.Language, Language> getSupportedLangauges() {
        return languages;
    }
}
