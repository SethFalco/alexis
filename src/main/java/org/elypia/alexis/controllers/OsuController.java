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

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.config.ApiConfig;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.elypiai.osu.*;
import org.elypia.elypiai.osu.data.OsuMode;
import org.slf4j.*;

import javax.inject.*;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Optional;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
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
