package org.elypia.alexis.i18n;

import org.apache.deltaspike.core.api.message.LocaleResolver;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Locale;

// TODO: TEMP
@Priority(Integer.MAX_VALUE)
@ApplicationScoped
@Alternative
public class AlexisLocaleResolver implements LocaleResolver {

    @Override
    public Locale getLocale() {
        return Locale.US;
    }
}
