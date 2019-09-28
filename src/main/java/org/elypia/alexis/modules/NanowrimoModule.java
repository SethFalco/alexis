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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.Handler;
import org.elypia.elypiai.common.core.ex.FriendlyException;
import org.elypia.elypiai.nanowrimo.Nanowrimo;
import org.elypia.elypiai.nanowrimo.data.NanoError;
import org.slf4j.*;

import javax.validation.constraints.Size;

@Module(name = "nano", aliases = {"nanowrimo", "nano", "nnwm"}, help = "nano.help")
public class NanowrimoModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(NanowrimoModule.class);

    private Nanowrimo nano;

    @Inject
    public NanowrimoModule(Nanowrimo nano) {
        this.nano = nano;
    }

    @Command(name = "nano.info", aliases = {"get", "info"}, help = "nano.info.help")
    public void getUser(
        CommandlerEvent<MessageReceivedEvent> event,
        @Param(name = "common.username", help = "nano.info.p.name.h") @Size(min = 1, max = 60) String name
    ) {
        nano.getUser(name, true).queue((user) -> {
            event.send(user);
        }, (ex) -> {
            if (ex instanceof FriendlyException) {
                FriendlyException fex = (FriendlyException)ex;

                switch (fex.getTag()) {
                    case NanoError.USER_DOES_NOT_EXIST: {
                        event.send("nano.no_user");
                        return;
                    }
                    case NanoError.USER_DOES_NOT_HAVE_A_CURRENT_NOVEL: {
                        event.send("nano.info.no_novel");
                        return;
                    }
                }
            }
            else
                logger.error("Failed to perform HTTP request!", ex);
        });
    }
}
