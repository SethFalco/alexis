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
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.elypia.alexis.discord.utils.DiscordUtils;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.commandler.annotation.*;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.newb.AsyncUtils;
import org.elypia.commandler.producers.MessageSender;
import org.elypia.commandler.utils.ChatUtils;
import org.elypia.elypiai.urbandictionary.*;
import org.elypia.retropia.core.requests.RestLatch;
import org.slf4j.*;

import javax.inject.Inject;
import java.util.stream.Stream;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@CommandController
@StandardCommand
public class UrbanDictionaryController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(UrbanDictionaryController.class);

    private final AlexisMessages messages;
    private final MessageSender sender;
    private final UrbanDictionary ud;

	@Inject
    public UrbanDictionaryController(AlexisMessages messages, MessageSender sender) {
        this.messages = messages;
        this.sender = sender;
        this.ud = new UrbanDictionary();
    }

    /**
     * TODO: Return results in the order of the queue
     *
     * @param terms
     * @param random
     */
    @Default
    @Static
    @StandardCommand
    public void getDefinitions(Message message, @Param String[] terms, @Param("false") boolean random) {
        RestLatch<DefineResult> latch = new RestLatch<>();

        Stream.of(terms)
            .map(String::toLowerCase)
            .distinct()
            .map(ud::getDefinitions)
            .forEach(latch::add);

        var contexts = AsyncUtils.copyContext();

        latch.queue((results) -> {
            var requestContext = AsyncUtils.applyContext(contexts);
            Object response;

            if (results.isEmpty()) {
                response = messages.udNoDefinitions();
            } else if (results.size() > 1) {
                EmbedBuilder builder = DiscordUtils.newEmbed(message);
                builder.setTitle("Urban Dictionary", "https://www.urbandictionary.com/");

                for (DefineResult result : results) {
                    if (!result.hasDefinitions())
                        continue;

                    Definition definition = result.getDefinition(random);
                    String definitionBody = definition.getDefinitionBody();
                    String ifTruncated = "... " + MarkdownUtil.maskedLink(messages.readMore(), definition.getPermaLink());
                    String value = ChatUtils.truncateAndAppend(definitionBody, MessageEmbed.VALUE_MAX_LENGTH, ifTruncated);
                    builder.addField(definition.getWord(), value, false);
                }

                builder.setFooter(messages.udTotalResults(builder.getFields().size()));
                response = builder;
            } else {
                DefineResult result = results.get(0);
                response = (result.hasDefinitions()) ? result.getDefinition(random) : messages.udNoDefinitions();
            }

            sender.send(response);
            requestContext.deactivate();
        });
    }
}
