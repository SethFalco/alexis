/*
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

package org.elypia.alexis.modules;

import com.google.inject.Inject;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.*;
import org.elypia.elypiai.twitch.*;
import org.slf4j.*;

@Module(name = "twitch", group = "Media", aliases = "twitch")
public class TwitchModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(TwitchModule.class);

    private LanguageInterface lang;
    private Twitch twitch;

    @Inject
    public TwitchModule(LanguageInterface lang, Twitch twitch) {
        this.lang = lang;
        this.twitch = twitch;
    }

    @Command(name = "info", aliases = {"get", "info"})
    public void getUser(CommandlerEvent<?> event, @Param(name = "username") String username) {
        TwitchQuery query = new TwitchQuery();
        query.addUsername(username);

        twitch.getUsers(query).queue(users -> {
//            if (users.isEmpty())
//                event.send("twitch.info.no_user", Map.of("username", username));
//            else
//                event.send(users.get(0));
        }, (ex) -> logger.error("Failed to perform HTTP request!", ex));
    }
}
