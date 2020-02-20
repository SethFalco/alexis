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

package org.elypia.alexis.controllers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.alexis.utils.DiscordUtils;
import org.elypia.comcord.EventUtils;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.managers.MessengerManager;
import org.elypia.elypiai.common.core.RestLatch;
import org.elypia.elypiai.urbandictionary.*;
import org.slf4j.*;

import javax.inject.*;
import java.util.Optional;

// TODO: Allow Urban Dictionary to specify which definition by index.
// TODO: Display the definition index so that it's repeatable
// TODO: If a definition doesn't exist, throws an exception
/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class UrbanDictionaryController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryController.class);

    private final UrbanDictionary ud;
    private final MessengerManager messenger;

	@Inject
    public UrbanDictionaryController(final MessengerManager messenger) {
        this.ud = new UrbanDictionary();
        this.messenger = messenger;
    }

	public void define(ActionEvent<Event, Message> event, String[] terms, boolean random) {
        Event source = event.getRequest().getSource();
        RestLatch<DefineResult> latch = new RestLatch<>();

        for (String term : terms)
            latch.add(ud.define(term));

        latch.queue((results) -> {
            MessageChannel channel = EventUtils.getMessageChannel(source);

            if (results.isEmpty())
                channel.sendMessage("No definitions were found.").queue();
            else if (results.size() == 1) {
                Message message = messenger.getProvider(event.getRequest().getIntegration(), Definition.class)
                    .provide(event, results.get(0).get().getDefinition(random));
                channel.sendMessage(message).queue();
            } else {
                EmbedBuilder builder = DiscordUtils.newEmbed(event);

                for (Optional<DefineResult> optDefinition : results) {
                    if (optDefinition.isPresent()) {
                        DefineResult result = optDefinition.get();
                        Definition definition = result.getDefinition(random);
                        builder.addField(definition.getWord(), definition.getDefinition(), false);
                    }
                }

                MessageEmbed messageEmbed = builder.build();
                channel.sendMessage(messageEmbed).queue();
            }
        });
    }
}
