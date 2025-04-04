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

import javax.validation.constraints.Min;

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.Configuration;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@Configuration(prefix = "application.translate.")
public interface TranslateConfig {

    /**
     * The max number of characters to cap when performing aggregate functions.
     * By aggregate functions, we're referring when looking to detect the
     * language for many strings in order to find a most common result.
     * If it goes over this cap, the implementation will remove
     * strings until it's within an acceptable range.
     * Set this to 0 (default) to default to no character cap for language detection.
     *
     * <strong>
     *     It's recommended to set this to an appropriate value
     *     for the applications usage. Google Translate is a billed API.
     *     For example: the detect language endpoint would cost $4~ to
     *     accept 200,000 characters worth of strings just to try detect
     *     the language used in a guild or channel? A more than reliable enough
     *     result can be obtained with less, massively reducing the potential cost.
     * </strong>
     */
    @Min(0)
    @ConfigProperty(name = "aggregate-character-cap", defaultValue = "0")
    int getAggregateCharacterCap();

    /** The location of the image to send when attributing Google Cloud Translate. */
    @ConfigProperty(name = "attribution-url")
    String getAttributionUrl();
}
