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

package org.elypia.alexis.modules;

import com.google.inject.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.elypia.commandler.CommandlerEvent;
import org.elypia.commandler.annotations.*;
import org.elypia.commandler.interfaces.Handler;
import org.elypia.commandler.managers.*;
import org.elypia.elypiai.common.core.RestLatch;
import org.elypia.elypiai.urbandictionary.*;
import org.slf4j.*;

// TODO: Allow Urban Dictionary to specify which definition by index.
@Singleton
@Module(name = "Urban Dictionary", aliases = {"ud", "urban", "urbandict", "urbandictionary"})
public class UrbanDictionaryModule implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryModule.class);

    private final ResponseManager responseManager;
    private final LanguageManager lang;
    private final UrbanDictionary ud;

	@Inject
    public UrbanDictionaryModule(ResponseManager responseManager, LanguageManager lang, UrbanDictionary ud) {
        this.responseManager = responseManager;
        this.lang = lang;
        this.ud = ud;
    }

    @Static
    @Command(name = "ud.define", aliases = "define")
	public void define(
        CommandlerEvent<GenericMessageEvent> event,
        @Param(name = "body") String[] terms,
        @Param(name = "random", defaultValue = "false") boolean random
    ) {
        RestLatch<DefineResult> latch = new RestLatch<>();

        for (String term : terms)
            latch.add(ud.define(term));

        latch.queue((results) -> {
            if (results.isEmpty())
                event.getSource().getChannel().sendMessage(lang.get(event, "urban.no_result"));
            else {
                Message message = (Message) responseManager.provide(event, results);
                event.getSource().getChannel().sendMessage(message).queue();
            }
        });
    }
}
