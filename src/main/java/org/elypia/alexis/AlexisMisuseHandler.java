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

package org.elypia.alexis;

import org.elypia.commandler.api.MisuseHandler;
import org.elypia.commandler.exceptions.misuse.MisuseException;
import org.slf4j.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class AlexisMisuseHandler implements MisuseHandler {

    private static Logger logger = LoggerFactory.getLogger(AlexisMisuseHandler.class);

    @Override
    public <X extends MisuseException> Object handle(X ex) {
        logger.error("A runtime exception occured.", ex);
        return "A teddy bear had been spotted stealing a line of code, it seems it broke some of our functionality.";
    }
}
