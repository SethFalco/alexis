/*
 * Copyright 2019-2020 Elypia CIC
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

package org.elypia.alexis.adapters;

import com.google.cloud.translate.Language;
import org.elypia.alexis.services.translate.TranslateService;
import org.elypia.commandler.annotation.stereotypes.ParamAdapter;
import org.elypia.commandler.api.Adapter;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.metadata.MetaParam;

import javax.inject.Inject;
import java.util.Objects;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ParamAdapter(Language.class)
public class LanguageAdapter implements Adapter<Language> {

    private final TranslateService translateService;

    @Inject
    public LanguageAdapter(final TranslateService translateService) {
        this.translateService = translateService;
    }

    @Override
    public Language adapt(String input, Class<? extends Language> type, MetaParam metaParam, ActionEvent<?, ?> event) {
        Objects.requireNonNull(input);

        for (Language language : translateService.getSupportedLanguages()) {
            if (language.getCode().equalsIgnoreCase(input) || language.getName().equalsIgnoreCase(input))
                return language;
        }

        return null;
    }
}
