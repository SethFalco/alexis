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

package org.elypia.alexis.controllers;

import com.github.twitch4j.helix.domain.User;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.alexis.services.TwitchService;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.dispatchers.standard.*;
import org.slf4j.*;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.util.Optional;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@StandardController
public class TwitchController implements Controller {

    /** Logging with SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(TwitchController.class);

    /** The minimum length a Twitch username can be. (We check this to avoid redundant requests.) */
    private static final int MIN_NAME_LENGTH = 4;

    /** The maximum length a Twitch username can be. (We check this to avoid redundant requests.) */
    private static final int MAX_NAME_LENGTH = 25;

    private final TwitchService twitchService;
    private final AlexisMessages messages;

    @Inject
    public TwitchController(final TwitchService twitchService, final AlexisMessages messages) {
        this.twitchService = twitchService;
        this.messages = messages;
        logger.info("Created instance of {}.", TwitchController.class);
    }

    @StandardCommand
    public Object getTwitchUser(@Param @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String username) {
        Optional<User> optUser = twitchService.getUser(username);

        if (optUser.isPresent())
            return optUser.get();

        return messages.twitchUserNotFound();
    }
}
