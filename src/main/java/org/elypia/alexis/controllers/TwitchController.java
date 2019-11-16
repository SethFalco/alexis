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

package org.elypia.alexis.controllers;

import com.github.twitch4j.*;
import com.github.twitch4j.helix.domain.*;
import org.elypia.alexis.config.TwitchConfig;
import org.elypia.commandler.api.Controller;
import org.hibernate.validator.constraints.Length;
import org.slf4j.*;

import javax.inject.*;
import java.util.List;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
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
