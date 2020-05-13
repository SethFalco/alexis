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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.EventUtils;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;
import org.elypia.commandler.managers.MessengerManager;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.common.core.RestLatch;
import org.elypia.elypiai.urbandictionary.*;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.*;

// TODO: Allow Urban Dictionary to specify which definition by index.
// TODO: Display the definition index so that it's repeatable
// TODO: If a definition doesn't exist, throws an exception
// TODO: Should UD return an optional?
/**
 * @author seth@elypia.org (Seth Falco)
 */
@ApplicationScoped
public class UrbanDictionaryController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryController.class);

    private final UrbanDictionary ud;
    private final MessengerManager messenger;
    private final AlexisMessages messages;

	@Inject
    public UrbanDictionaryController(final MessengerManager messenger, AlexisMessages messages) {
        this.ud = new UrbanDictionary();
        this.messenger = messenger;
        this.messages = messages;
    }

	public void define(ActionEvent<Event, Message> event, String[] terms, boolean random) {
        Event source = event.getRequest().getSource();
        RestLatch<DefineResult> latch = new RestLatch<>();

        List<String> termsList = Stream.of(terms)
            .map(String::toLowerCase)
            .distinct()
            .collect(Collectors.toList());

        for (String term : termsList)
            latch.add(ud.define(term));

        // TODO: Return results in the order of the queue
        latch.queue((results) -> {
            MessageChannel channel = EventUtils.getMessageChannel(source);

            if (results.isEmpty())
                channel.sendMessage(messages.udNoDefinitions()).queue();
            else if (results.size() == 1) {
                Optional<DefineResult> optResult = results.get(0);

                if (optResult.isPresent()) {
                    DefineResult result = optResult.get();

                    if (!result.getDefinitions().isEmpty()) {
                        Definition definition = result.getDefinition(random);
                        Message message = messenger.getMessenger(event.getRequest().getIntegration(), Definition.class).provide(event, definition);
                        channel.sendMessage(message).queue();
                    } else {
                        channel.sendMessage(messages.udNoDefinitions()).queue();
                    }
                }
            } else {
                EmbedBuilder builder = DiscordUtils.newEmbed(event);
                builder.setTitle("Urban Dictionary", "https://www.urbandictionary.com/");
                builder.setFooter(messages.udTotalResults(results.size()));

                for (Optional<DefineResult> optDefinition : results) {
                    if (optDefinition.isPresent()) {
                        DefineResult result = optDefinition.get();
                        Definition definition = result.getDefinition(random);
                        String definitionBody = definition.getDefinition();
                        String value = ChatUtils.truncateAndAppend(definitionBody, MessageEmbed.VALUE_MAX_LENGTH,  "... " + MarkdownUtil.maskedLink("Read More", definition.getPermaLink()));
                        builder.addField(definition.getWord(), value, false);
                    }
                }

                MessageEmbed messageEmbed = builder.build();
                channel.sendMessage(messageEmbed).queue();
            }
        });
    }
}
