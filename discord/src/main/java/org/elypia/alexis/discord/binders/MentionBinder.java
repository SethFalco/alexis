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

package org.elypia.alexis.discord.binders;

import net.dv8tion.jda.api.events.Event;
import org.elypia.commandler.Request;
import org.elypia.commandler.api.HeaderBinder;

import javax.inject.Singleton;
import java.util.*;

/**
 * Add the bots self-mentions to the headers so that they
 * can be referenced internally.
 *
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
