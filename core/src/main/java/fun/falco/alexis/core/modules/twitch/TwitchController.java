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

package fun.falco.alexis.core.modules.twitch;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.constraints.Size;

import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.dispatchers.standard.StandardCommand;
import org.elypia.commandler.dispatchers.standard.StandardController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.twitch4j.helix.domain.User;

import fun.falco.alexis.core.i18n.AlexisMessages;

/**
 * @author seth@falco.fun (Seth Falco)
 */
@StandardController
public class TwitchController {

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

        if (optUser.isPresent()) {
            return optUser.get();
        }

        return messages.twitchUserNotFound();
    }
}
