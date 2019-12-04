/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.config;

import org.elypia.commandler.config.ConfigService;

import javax.inject.*;
import javax.validation.constraints.NotNull;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class TranslationConfig {

    /** The location of the image to send when attributing Google Cloud Translate. */
    @NotNull
    private final String attributionUrl;

    @Inject
    public TranslationConfig(final ConfigService config) {
        attributionUrl = config.getString("alexis.translate.attribution-url");
    }

    public String getAttributionUrl() {
        return attributionUrl;
    }
}
