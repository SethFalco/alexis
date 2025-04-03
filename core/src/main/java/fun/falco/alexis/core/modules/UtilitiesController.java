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

package fun.falco.alexis.core.modules;

import javax.inject.Inject;

import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;

import fun.falco.alexis.core.i18n.AlexisMessages;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class UtilitiesController {

    private final AlexisMessages messages;

    @Inject
    public UtilitiesController(final AlexisMessages messages) {
        this.messages = messages;
    }

    @StandardCommand(isStatic = true)
    public String count(@Param String input) {
        return messages.utilitiesTotalCharacters(input.length());
    }
}
