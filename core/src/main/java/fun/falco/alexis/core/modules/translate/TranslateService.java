/*
 * Copyright 2019-2025 Seth Falco and Alexis Contributors
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

package fun.falco.alexis.core.modules.translate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@ApplicationScoped
public class TranslateService {

    private static final Logger logger = LoggerFactory.getLogger(TranslateService.class);

    /** The configuration for any Google Translate related code. */
    private final TranslateConfig translateConfig;

    /** The client to access the Google Translate API. */
    private final Translate translate;

    /** Shouldn't be used directly other than in the {@link #getSupportedLanguages()} method. */
    private Collection<Language> supported;

    @Inject
    public TranslateService(TranslateConfig translateConfig) throws IOException {
        this.translateConfig = Objects.requireNonNull(translateConfig);
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

        translate = TranslateOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .getService();
    }

    /**
     * @param strings Strings to detect the language of.
     * @return Language that comes up most often in a list.
     */
    public Language detectMostFrequentAsLanguage(List<String> strings) {
        String language = detectMostFrequent(strings);
        return findLanguage(language);
    }

    /**
     * Detect the language of a list of strings, and then return the language
     * which is detected most frequently in this list.
     *
     * <p>This uses the {@link TranslateConfig#getAggregateCharacterCap()}
     * setting to cap out how many characters before making a request.</p>
     *
     * @param strings Strings to detect the language of.
     * @return Language that comes up most often in a list.
     */
    public String detectMostFrequent(List<String> strings) {
        int characterCap = translateConfig.getAggregateCharacterCap();
        int totalCharacters = strings.stream().mapToInt(String::length).sum();
        boolean shouldApplyCap = characterCap > 0 && characterCap < totalCharacters;
        List<String> stringsToDetect = (shouldApplyCap) ? applyCap(strings, characterCap) : strings;
        List<Detection> detections = detect(stringsToDetect);

        if (detections.isEmpty()) {
            throw new IllegalStateException("Unable to detect language, no strings were provided.");
        }

        return detections.stream()
            .collect(Collectors.groupingBy(Detection::getLanguage))
            .entrySet()
            .stream()
            .max(Comparator.comparingInt((entry) -> entry.getValue().size()))
            .orElseThrow()
            .getKey();
    }

    public List<Detection> detect(List<String> strings) {
        if (strings.isEmpty()) {
            return List.of();
        }

        return translate.detect(strings);
    }

    /**
     * Apply the specified cap to ensure we don't try to
     * detect the language from any more characters than specified.
     *
     * @param strings Candidates to apply detection on.
     * @param characterCap Character cap, the result shouldn't have more characters than this.
     * @return Truncated list of strings will elements removed from where the total would exceed the cap.
     */
    private List<String> applyCap(List<String> strings, int characterCap) {
        List<String> stringsToDetect = new ArrayList<>();
        int charactersToDetect = 0;

        for (String string : strings) {
            int stringLength = string.length();

            if (charactersToDetect + stringLength > characterCap) {
                break;
            }

            stringsToDetect.add(string);
            charactersToDetect += stringLength;
        }

        logger.info("Truncated list of strings from {} items to {} items ({} characters).", strings.size(), stringsToDetect.size(), charactersToDetect);
        return stringsToDetect;
    }

    public Translation translate(String text, Locale locale) {
        for (Language language : getSupportedLanguages()) {
            if (language.getCode().equalsIgnoreCase(locale.getLanguage())) {
                return translate(text, language);
            }
        }

        throw new IllegalArgumentException("This isn't a supported language.");
    }

    public Translation translate(String text, Language language) {
        return translate(text, language.getCode());
    }

    public Translation translate(String text, String languageCode) {
        return translate.translate(text, Translate.TranslateOption.targetLanguage(languageCode));
    }

    public Language findLanguage(String language) {
        for (Language supportedLanguage : getSupportedLanguages()) {
            if (supportedLanguage.getCode().equals(language)) {
                return supportedLanguage;
            }
        }

        return null;
    }

    /**
     * @return List of languages support by the Google Translate API.
     */
    public Collection<Language> getSupportedLanguages() {
        return (supported == null) ? supported = translate.listSupportedLanguages() : supported;
    }
}
