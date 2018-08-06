package com.elypia.alexis.commandler.validators;

import com.elypia.alexis.Alexis;
import com.elypia.commandler.jda.*;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.elypiai.utils.Language;

public class SupportedValidator implements IJDAParamValidator<Language, Supported> {

    @Override
    public boolean validate(JDACommand event, Language language, Supported supported, MetaParam param) {
        return Alexis.getChatbot().getSupportedLanguages().contains(language);
    }

    @Override
    public String help(Supported annotation) {
        return "This must be a language that Alexis supports!";
    }
}
