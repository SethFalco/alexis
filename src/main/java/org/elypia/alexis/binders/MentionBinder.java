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

package org.elypia.alexis.binders;

import net.dv8tion.jda.api.events.Event;
import org.elypia.commandler.Request;
import org.elypia.commandler.api.HeaderBinder;

import javax.inject.Singleton;
import java.util.*;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class MentionBinder implements HeaderBinder {

    @Override
    public <S, M> Map<String, String> bind(Request<S, M> request) {
        Event source = (Event)request.getSource();
        String id = source.getJDA().getSelfUser().getId();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("SELF_MENTION", "<@" + id + ">");
        headers.put("SELF_NICK_MENTION", "<@!" + id + ">");
        return headers;
    }
}
