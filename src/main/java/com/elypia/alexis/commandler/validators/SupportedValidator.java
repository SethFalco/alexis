package com.elypia.alexis.commandler.validators;

import com.elypia.commandler.jda.*;
import com.elypia.commandler.metadata.MetaParam;
import com.elypia.elypiai.utils.Language;

public class SupportedValidator extends IJDAParamValidator<Language, Supported> {

    public SupportedValidator() {
        super((o) -> "This must be a language that Alexis supports!");
    }

    @Override
    public boolean validate(JDACommand event, Language language, Supported supported, MetaParam param) {
//        return Alexis.supportedLanguages.contains(language);
        return false;
    }
}
