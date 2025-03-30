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

package org.elypia.alexis.core.configuration;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.Configuration;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@Configuration(prefix = "application.author.")
public interface AuthorConfig {

    /** Name of the author of the bot. */
    @ConfigProperty(name = "name")
    String getName();

    /** URL linking to a resource of the author. */
    @ConfigProperty(name = "url")
    String getUrl();

    /** Logo or icon that represents the author of the bot. */
    @ConfigProperty(name = "icon-url")
    String getIconUrl();

    /** Support guild to get help with the bot. */
    @ConfigProperty(name ="support-guild-id")
    Long getSupportGuildId();
}
