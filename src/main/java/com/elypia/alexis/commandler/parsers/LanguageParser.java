package com.elypia.alexis.commandler.parsers;

import com.elypia.alexis.utils.*;
import com.elypia.commandler.annotations.Compatible;
import com.elypia.commandler.metadata.ParamData;
import com.elypia.jdac.alias.*;

import java.util.Map;

@Compatible(Language.class)
public class LanguageParser implements IJDACParser<Language> {

    private static final Map<String, Language> OTHER = Map.of(
        "baguette", Language.FRENCH,
        "tea", Language.ENGLISH,
        "butt stuff", Language.GERMAN,
        "kurwa", Language.POLISH,
        "pizza", Language.ITALIAN
    );

    @Override
    public Language parse(JDACEvent event, ParamData data, Class<? extends Language> type, String input) {
        for (Language language : Language.values()) {
            if (language.getName().equalsIgnoreCase(input))
                return language;

            if (language.getCode().equalsIgnoreCase(input))
                return language;

            for (Country country : language.getCountries()) {
                if (country.getUnicodeEmote().equalsIgnoreCase(input))
                    return language;

                if (country.getCountryName().equalsIgnoreCase(input))
                    return language;

                if (country.getIso3166().equalsIgnoreCase(input))
                    return language;

                if (country.getIsoCode().equalsIgnoreCase(input))
                    return language;
            }
        }

        for (Map.Entry<String, Language> entry : OTHER.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(input))
                return entry.getValue();
        }

        return null;
    }
}
