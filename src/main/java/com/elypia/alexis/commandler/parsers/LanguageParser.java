package com.elypia.alexis.commandler.parsers;

import com.elypia.commandler.jda.*;
import com.elypia.elypiai.utils.*;

public class LanguageParser implements IJDAParser<Language> {

    @Override
    public Language parse(JDACommand event, Class<? extends Language> type, String input) {
        for (Language language : Language.values()) {
            if (language.getCode().equalsIgnoreCase(input))
                return language;

            if (language.getName().equalsIgnoreCase(input))
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

        // Some bonus ones. <3

        if (input.equalsIgnoreCase("baguette"))
            return Language.FRENCH;

        if (input.equalsIgnoreCase("tea"))
            return Language.ENGLISH;

        if (input.equalsIgnoreCase("butt stuff"))
            return Language.GERMAN;

        if (input.equalsIgnoreCase("kurwa"))
            return Language.POLISH;

        if (input.equalsIgnoreCase("pizza"))
            return Language.ITALIAN;

        event.invalidate("Couldn't find a langauge to match up with your input.");
        return null;
    }
}
