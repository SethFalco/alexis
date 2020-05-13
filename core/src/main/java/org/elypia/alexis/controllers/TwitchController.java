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

import com.github.twitch4j.*;
import com.github.twitch4j.helix.domain.*;
import org.elypia.alexis.config.TwitchConfig;
import org.elypia.commandler.api.Controller;
import org.hibernate.validator.constraints.Length;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class TwitchController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(TwitchController.class);

    private final TwitchClient twitch;

    @Inject
    public TwitchController(final TwitchConfig config) {
        this.twitch = TwitchClientBuilder.builder()
            .withEnableHelix(true)
            .withClientId(config.getClientId())
            .withClientSecret(config.getClientSecret())
            .build();

        logger.info("Succesfully authenticated to the Twitch API.");
    }

    public User info(@Length(min = 4, max = 25) String username) {
        UserList users = twitch.getHelix().getUsers(null, null, List.of(username)).execute();
        return users.getUsers().get(0);
    }
}
