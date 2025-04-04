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

package fun.falco.alexis.discord.binders;

import java.util.HashMap;
import java.util.Map;

import org.elypia.commandler.annotation.stereotypes.Binder;
import org.elypia.commandler.api.HeaderBinder;
import org.elypia.commandler.event.Request;

import net.dv8tion.jda.api.events.Event;

/**
 * Add the bots self-mentions to the headers so that they
 * can be referenced internally.
 *
 * @author seth@falco.fun (Seth Falco)
 */
@Binder
public class MentionBinder implements HeaderBinder {

    @Override
    public <S, M> Map<String, String> bind(Request<S, M> request) {
        Event source = (Event) request.getSource();
        String id = source.getJDA().getSelfUser().getId();

        HashMap<String, String> headers = new HashMap<>();
        headers.put("SELF_MENTION", "<@" + id + ">");
        headers.put("SELF_NICK_MENTION", "<@!" + id + ">");

        return headers;
    }
}
