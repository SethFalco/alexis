package com.elypia.alexis.commandler.adapters;

import com.elypia.alexis.utils.*;
import com.elypia.commandler.CommandlerEvent;
import com.elypia.commandler.annotations.Adapter;
import com.elypia.commandler.interfaces.ParamAdapter;
import com.elypia.commandler.metadata.data.MetaParam;

import java.util.Map;

// TODO: Instead of using Language, see if I can use Locale
@Adapter(Language.class)
public class LanguageAdapter implements ParamAdapter<Language> {

    private static final Map<String, Language> OTHER = Map.of(
        "baguette", Language.FRENCH,
        "tea", Language.ENGLISH,
        "butt stuff", Language.GERMAN,
        "kurwa", Language.POLISH,
        "pizza", Language.ITALIAN
    );

    @Override
    public Language adapt(String input, Class<? extends Language> type, MetaParam param, CommandlerEvent<?> event) {
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
