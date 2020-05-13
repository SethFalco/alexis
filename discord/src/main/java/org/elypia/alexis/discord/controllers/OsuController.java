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

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.config.ApiConfig;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.elypiai.osu.*;
import org.elypia.elypiai.osu.data.OsuMode;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Optional;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class OsuController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(OsuController.class);

    private final Osu osu;

    @Inject
    public OsuController(final ApiConfig config) {
        this.osu = new Osu(config.getOsu());
    }

    public Object get(ActionEvent<Event, Message> event, @Size(min = 3, max = 15) String username, OsuMode mode) throws IOException {
        Optional<Player> player = osu.getPlayer(username, mode).complete();

        if (player.isEmpty())
            return "No player was found.";

        return player.get();
    }
}
